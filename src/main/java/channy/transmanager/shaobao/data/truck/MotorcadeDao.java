package channy.transmanager.shaobao.data.truck;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONException;
import org.xml.sax.SAXException;

import channy.transmanager.shaobao.data.BaseDao;
import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.model.vehicle.Motorcade;
import channy.util.ChannyException;

public class MotorcadeDao extends BaseDao<Motorcade> {

	// public static boolean exists(String name) {
	// return MotorcadeDao.getByName(name) == null;
	// }

	// public static void add(String name) throws XPathExpressionException,
	// SAXException, IOException, ParserConfigurationException, ChannyException
	// {
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	//
	// Motorcade motorcade = new Motorcade();
	// motorcade.setName(name);
	// motorcade.setDateCreated(new Date());
	// Set<Truck> trucks = new HashSet<Truck>();
	// Truck t = new Truck();
	// t.setPlate("粤F000001");
	// trucks.add(t);
	// motorcade.setTrucks(trucks);
	// session.save(motorcade);
	//
	// session.getTransaction().commit();
	// }
	//
	// public static int getCount(Map<String, Object> filter) throws
	// ChannyException {
	// String hql = "select count(*) from Motorcade motorcade";
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
	// public static Motorcade getByName(String name) {
	// String hql = "from Motorcade motorcade where motorcade.name = :name";
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query = session.createQuery(hql);
	// query.setParameter("name", name);
	// @SuppressWarnings("unchecked")
	// List<Motorcade> result = query.list();
	// session.getTransaction().commit();
	// if (result.size() == 0) {
	// return null;
	// } else {
	// return result.get(0);
	// }
	// }
	//
	// public static void remove(String name) throws ChannyException {
	// String hql = "from Motorcade motorcade where motorcade.name = :name";
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query = session.createQuery(hql);
	// query.setParameter("name", name);
	//
	// @SuppressWarnings("unchecked")
	// List<Motorcade> list = query.list();
	//
	// if (list.size() == 0) {
	// throw new ChannyException(ErrorCode.OBJECT_NOT_EXISTED);
	// }
	//
	// for (Motorcade motorcade : list) {
	// for (Truck truck : motorcade.getTrucks()) {
	// truck.setMotorcade(null);
	// }
	// session.delete(motorcade);
	// }
	//
	// session.getTransaction().commit();
	// }
	//
	// public static JSONObject query(int page, int pageSize, Map<String,
	// Object> filter) throws JSONException, ChannyException {
	// int total = MotorcadeDao.getCount(null);
	// int match = MotorcadeDao.getCount(filter);
	//
	// String hql = "select motorcade from Motorcade motorcade";
	// if (filter != null && filter.size() > 0) {
	// hql = String.format("select count(*) %s",
	// HibernateUtil.assembleHql(filter));
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
	// List<Motorcade> list = query.list();
	// session.getTransaction().commit();
	//
	// JSONObject result = new JSONObject();
	// JSONArray data = new JSONArray();
	// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// for (Motorcade motorcade : list) {
	// JSONObject obj = new JSONObject();
	// obj.put("name", motorcade.getName());
	// obj.put("dateCreated", format.format(motorcade.getDateCreated()));
	// JSONArray trucks = new JSONArray();
	// for (Truck truck : motorcade.getTrucks()) {
	// trucks.put(truck.getPlate());
	// }
	//
	// obj.put("trucks", trucks);
	//
	// data.put(obj);
	// }
	//
	// result.put("total", total);
	// result.put("match", match);
	// result.put("data", data);
	//
	// return result;
	// }
	//
	// public static JSONObject querySelect(int page, int pageSize, Map<String,
	// Object> filter) throws JSONException, ChannyException {
	// int match = MotorcadeDao.getCount(filter);
	//
	// String hql = "from Motorcade motorcade";
	// // String condition = "";
	// if (filter != null && filter.size() > 0) {
	// // hql += " where";
	// // condition = HibernateUtil.assembleHqlCondition(filter, "motorcade");
	// // hql += condition;
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
	// List<Motorcade> list = query.list();
	// session.getTransaction().commit();
	//
	// JSONArray data = new JSONArray();
	// for (Motorcade motorcade : list) {
	// JSONObject obj = new JSONObject();
	// obj.put("id", motorcade.getId());
	// obj.put("text", motorcade.getName());
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
		// MotorcadeDao.add("韶宝");
		// System.out.println(getByName("韶"));
		//System.out.println(query(0, 15, null));
		// System.out.println(getCount(null));
		// MotorcadeDao.remove("韶");
	}

	@Override
	public Motorcade getById(long id) {
		return super.getById(id, Motorcade.class);
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, Motorcade.class);
	}

	@Override
	public QueryResult<Motorcade> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, Motorcade.class);
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(Motorcade.class, filter);
	}
	
	public Motorcade getByName(String name) {
		List<Motorcade> result = super.getByField("name", name, Motorcade.class);
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}
}
