package channy.transmanager.shaobao.service.vehicle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.data.truck.TruckDao;
import channy.transmanager.shaobao.model.user.Driver;
import channy.transmanager.shaobao.model.user.User;
import channy.transmanager.shaobao.model.vehicle.Motorcade;
import channy.transmanager.shaobao.model.vehicle.Truck;
import channy.transmanager.shaobao.service.ServiceInterface;
import channy.transmanager.shaobao.service.user.DriverService;
import channy.util.ChannyException;
import channy.util.ErrorCode;

public class TruckService implements ServiceInterface<Truck> {
	private TruckDao dao = new TruckDao();

	
	public JSONObject select(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException {
		QueryResult<Truck> result = dao.query(page, pageSize, filter);

		List<Truck> list = result.getData();
		JSONObject res = new JSONObject();
		res.put("match", result.getMatch());

		JSONArray array = new JSONArray();
		for (Truck e : list) {
			JSONObject obj = new JSONObject();
			obj.put("id", e.getId());
			obj.put("text", e.getPlate());

			array.put(obj);
		}

		res.put("data", array);

		return res;
	}

	public Truck add(String plate, String description) throws ChannyException {
		if (dao.getByPlate(plate) != null) {
			throw new ChannyException(ErrorCode.OBJECT_EXISTED);
		}
		Truck truck = new Truck();
		truck.setPlate(plate);
		truck.setDescription(description);
		dao.add(truck);
		return truck;
	}

	public Truck add(String plate, String motorcade, String description) throws ChannyException {
		if (dao.getByPlate(plate) != null) {
			throw new ChannyException(ErrorCode.OBJECT_EXISTED);
		}

		MotorcadeService s = new MotorcadeService();
		Motorcade m = s.getByName(motorcade);
		if (m == null) {
			m = s.add(motorcade, "系统自动添加");
		}
		Truck truck = new Truck();
		truck.setPlate(plate);
		truck.setDescription(description);
		truck.setMotorcade(m);
		dao.add(truck);
		return truck;
	}

	public Truck add(String plate, Motorcade motorcade, String description) throws ChannyException {
		if (dao.getByPlate(plate) != null) {
			throw new ChannyException(ErrorCode.OBJECT_EXISTED);
		}
		Truck truck = new Truck();
		truck.setPlate(plate);
		truck.setDescription(description);
		truck.setMotorcade(motorcade);
		dao.add(truck);
		return truck;
	}

	public Truck getByPlate(String plate) {
		return dao.getByPlate(plate);
	}

	
	public void update(Truck truck) {
		dao.update(truck);
	}

	
	public Truck getById(long id) {
		return dao.getById(id);
	}

	
	public void remove(Truck entity) {
		dao.remove(entity);
	}

	
	public void removeById(long id) throws ChannyException {
		dao.removeById(id);
	}

	
	public boolean exists(long id) {
		return dao.exists(id);
	}

	
	public int getCount() throws ChannyException {
		return dao.getCount(null);
	}

	
	public JSONObject query(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	public Truck getNextCandidate() {
		return dao.scheduleTruck();
	}

	public List<Truck> getAvailableTrucks() {
		return dao.getAvailableTrucks();
	}

	public void importTruck(String fileName) throws XPathExpressionException, SAXException, ParserConfigurationException, ChannyException {
		DriverService service = new DriverService();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("\t");
				// for (String part : parts) {
				// System.out.println(part);
				// }
				System.out.println(line);

				if (getByPlate(parts[0]) != null) {
					continue;
				}
				Truck truck = add(parts[0], "韶宝", null);

				List<User> drivers = service.getByName(parts[1]);
				if (drivers.isEmpty()) {
					continue;
				}

				Driver driver = (Driver) drivers.get(0);
				driver.setTruck(truck);
				truck.setDriver(driver);
				//service.update(driver);
				update(truck);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	
	public static void main(String[] args) throws XPathExpressionException, SAXException, ParserConfigurationException, ChannyException {
		TruckService service = new TruckService();
		service.importTruck("/Users/Channy/Desktop/Channy's/shaobao/drivers");
		
		//Truck truck = service.getNextCandidate();
		//System.out.println(truck.getPlate());
		//System.out.println(truck.getDriver().getName());
	}
}
