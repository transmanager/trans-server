package channy.transmanager.shaobao.data.truck;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONException;
import org.xml.sax.SAXException;

import channy.transmanager.shaobao.data.BaseDao;
import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.model.vehicle.Truck;
import channy.transmanager.shaobao.model.vehicle.TruckStatus;
import channy.util.ChannyException;
import channy.util.HibernateUtil;

public class TruckDao extends BaseDao<Truck> {
	// public static boolean exists(String plate) {
	// return TruckDao.getByPlate(plate) != null;
	// }
	//
	// public static Truck getByPlate(String plate) {
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query =
	// session.createQuery("from Truck truck where truck.plate = :plate");
	// query.setParameter("plate", plate);
	// @SuppressWarnings("unchecked")
	// List<Truck> result = query.list();
	// session.getTransaction().commit();
	// if (result.size() == 0) {
	// return null;
	// } else {
	// return result.get(0);
	// }
	// }
	//
	// public static int getCount(Map<String, Object> filter) throws
	// ChannyException {
	// String hql = "select count(*) from Truck truck";
	// if (filter != null && filter.size() > 0) {
	// hql = String.format("select count(*) %s",
	// HibernateUtil.assembleHql(filter));
	// }
	//
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query = session.createQuery(hql);
	// HibernateUtil.assembleQuery(query, filter);
	// @SuppressWarnings("rawtypes")
	// List list = query.list();
	// session.getTransaction().commit();
	//
	// Integer count = Integer.parseInt(list.get(0).toString());
	// return count;
	// }
	//
	// public static void add(String motorcade, String plate) throws
	// XPathExpressionException, SAXException, IOException,
	// ParserConfigurationException,
	// ChannyException {
	// Motorcade m = MotorcadeDao.getByName(motorcade);
	// if (m == null) {
	// m = new Motorcade();
	// m.setName(motorcade);
	// m.setDateCreated(new Date());
	// }
	// Truck truck = new Truck();
	// truck.setMotorcade(m);
	// truck.setPlate(plate);
	// truck.setLastOrder(null);
	// truck.setDateCreated(new Date());
	// truck.setAge(0);
	// truck.setOdometer(0);
	// truck.setStatus(TruckStatus.Idle);
	//
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// session.save(truck);
	//
	// session.getTransaction().commit();
	// }
	//
	// public static void update(Truck truck) {
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// session.update(truck);
	// session.getTransaction().commit();
	// }
	//
	// public static void remove(long id) throws ChannyException {
	// String hql = String.format("from Truck truck where truck.id = :id");
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query = session.createQuery(hql);
	// query.setParameter("id", id);
	//
	// @SuppressWarnings("unchecked")
	// List<Truck> list = query.list();
	//
	// if (list.size() == 0) {
	// throw new ChannyException(ErrorCode.OBJECT_NOT_EXISTED);
	// }
	//
	// for (Truck truck : list) {
	// session.delete(truck);
	// }
	//
	// session.getTransaction().commit();
	// }
	//
	// public static void add(String plate, int age, int odometer) throws
	// XPathExpressionException, SAXException, IOException,
	// ParserConfigurationException, ChannyException {
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	//
	// Truck truck = new Truck();
	// truck.setPlate(plate);
	// truck.setDateCreated(new Date());
	// truck.setAge(age);
	// truck.setOdometer(odometer);
	// truck.setStatus(TruckStatus.Idle);
	// truck.setLastOrder(null);
	// session.save(truck);
	//
	// session.getTransaction().commit();
	// }

	// public static void importTruck(String fileName) throws
	// XPathExpressionException, SAXException, ParserConfigurationException,
	// ChannyException {
	// File file = new File(fileName);
	// BufferedReader reader = null;
	// try {
	// reader = new BufferedReader(new FileReader(file));
	// String line = null;
	// while ((line = reader.readLine()) != null) {
	// String[] parts = line.split("\t");
	// // for (String part : parts) {
	// // System.out.println(part);
	// // }
	// System.out.println(line);
	//
	// TruckDao.add("韶宝", parts[0]);
	//
	// Driver driver = DriverDao.getByName(parts[1]);
	// if (driver == null) {
	// continue;
	// }
	//
	// Truck truck = TruckDao.getByPlate(parts[0]);
	// if (truck == null) {
	// continue;
	// }
	//
	// driver.setTruck(truck);
	// truck.setDriver(driver);
	// UserDao.update(driver);
	// TruckDao.update(truck);
	// }
	// reader.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// if (reader != null) {
	// try {
	// reader.close();
	// } catch (IOException e1) {
	// }
	// }
	// }
	// }

	// public static JSONObject querySelect(int page, int pageSize, Map<String,
	// Object> filter) throws JSONException, ChannyException {
	// int match = TruckDao.getCount(filter);
	//
	// String hql = "from Truck truck";
	// // String condition = "";
	// if (filter != null && filter.size() > 0) {
	// hql = HibernateUtil.assembleHql(filter);
	// }
	//
	// int offset = page * pageSize;
	// if (offset >= match) {
	// offset = (match - 1) / pageSize * pageSize;
	// }
	//
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query = session.createQuery(hql);
	// HibernateUtil.assembleQuery(query, filter);
	// query.setFirstResult(offset);
	// query.setMaxResults(pageSize);
	//
	// @SuppressWarnings("unchecked")
	// List<Truck> list = query.list();
	// session.getTransaction().commit();
	//
	// JSONArray data = new JSONArray();
	// for (Truck truck : list) {
	// JSONObject obj = new JSONObject();
	// obj.put("id", truck.getId());
	// obj.put("text", truck.getPlate());
	// data.put(obj);
	// }
	//
	// JSONObject result = new JSONObject();
	// result.put("results", data);
	// result.put("total", match);
	// return result;
	// }

	public static void main(String[] args) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, ChannyException,
			JSONException {
		// TruckDao.add("韶宝", "粤F000002");

		// Truck truck = getByPlate("粤F000004");
		// System.out.println(truck.getMotorcade().getName());

		// System.out.println(truck.getPlate());

		// truck.setMotorcade(motorcade);
		// update(truck);

		// TruckDao.remove("粤F000000");
		// TruckDao.remove("粤F000001");

		// importTruck("/Users/Channy/Desktop/Channy's/shaobao/drivers");

		// Map<String, Object> filter = new HashMap<String, Object>();
		// filter.put(HibernateUtil.ClassKey, Truck.class);
		// filter.put("plate", "12");
		// System.out.println(querySelect(0, 10, filter).toString());

		// System.out.println(getNextCandidate());
	}

	public Truck getByPlate(String plate) {
		List<Truck> result = super.getByField("plate", plate, Truck.class);
		if (result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}

	@Override
	public Truck getById(long id) {
		return super.getById(id, Truck.class);
	}

	@Override
	public boolean exists(long id) {
		return getById(id) != null;
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, Truck.class);
	}

	@Override
	public QueryResult<Truck> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, Truck.class);
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(Truck.class, filter);
	}

	public List<Truck> getAvailableTrucks() {
		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from Truck truck where truck.status = :status order by truck.lastOrder desc");
			query.setParameter("status", TruckStatus.Idle);
			@SuppressWarnings("unchecked")
			List<Truck> result = query.list();
			for (Truck truck : result) {
				truck.getMotorcade().getName();
			}
			return result;
		} finally {
			session.getTransaction().commit();
		}
	}

	public Truck scheduleTruck() {
		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from Truck truck where truck.status = :status order by truck.lastOrder desc");
			query.setParameter("status", TruckStatus.Idle);
			query.setFirstResult(0);
			query.setMaxResults(1);
			@SuppressWarnings("unchecked")
			List<Truck> result = query.list();
			for (Truck truck : result) {
				truck.getMotorcade().getName();
			}
			if (result.isEmpty()) {
				return null;
			}

			return result.get(0);
		} finally {
			session.getTransaction().commit();
		}
	}
}
