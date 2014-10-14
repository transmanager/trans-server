package channy.transmanager.shaobao.service.vehicle;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.data.truck.MotorcadeDao;
import channy.transmanager.shaobao.model.vehicle.Motorcade;
import channy.transmanager.shaobao.model.vehicle.Truck;
import channy.transmanager.shaobao.service.ServiceInterface;
import channy.util.ChannyException;

public class MotorcadeService implements ServiceInterface<Motorcade> {
	private MotorcadeDao dao = new MotorcadeDao();
	
	public Motorcade add(String name, String description) {
		Motorcade motorcade = new Motorcade();
		motorcade.setName(name);
		motorcade.setDescription(description);
		
		dao.add(motorcade);
		return motorcade;
	}
	
	public Motorcade add(String name, String description, Set<Truck> trucks) {
		Motorcade motorcade = new Motorcade();
		motorcade.setName(name);
		motorcade.setDescription(description);
		motorcade.setTrucks(trucks);
		
		dao.add(motorcade);
		return motorcade;
	}
	
	public JSONObject query(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException {
		QueryResult<Motorcade> result = dao.query(page, pageSize, filter);
		
		List<Motorcade> list = result.getData();
		JSONObject res = new JSONObject();
		res.put("total", result.getTotal());
		res.put("match", result.getMatch());

		JSONArray array = new JSONArray();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (Motorcade e : list) {
			JSONObject obj = new JSONObject();
			obj.put("id", e.getId());
			obj.put("name", e.getName());
			obj.put("description", e.getDescription());
			obj.put("dateCreated", format.format(e.getDateCreated()));
			
			array.put(obj);
		}
		
		res.put("data", array);
		
		return res;
	}
	
	public JSONObject select(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException {
		QueryResult<Motorcade> result = dao.query(page, pageSize, filter);
		
		List<Motorcade> list = result.getData();
		JSONObject res = new JSONObject();
		res.put("match", result.getMatch());

		JSONArray array = new JSONArray();
		for (Motorcade e : list) {
			JSONObject obj = new JSONObject();
			obj.put("id", e.getId());
			obj.put("text", e.getName());
			
			array.put(obj);
		}
		
		res.put("data", array);
		
		return res;
	}
	
	public Motorcade getByName(String name) {
		return dao.getByName(name);
	}
	
	
	public Motorcade getById(long id) {
		return dao.getById(id);
	}

	
	public void update(Motorcade entity) {
		dao.update(entity);
	}

	
	public void remove(Motorcade entity) {
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
}
