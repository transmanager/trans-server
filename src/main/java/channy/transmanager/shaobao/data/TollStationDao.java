package channy.transmanager.shaobao.data;

import java.util.List;
import java.util.Map;

import channy.transmanager.shaobao.model.TollStation;
import channy.util.ChannyException;

public class TollStationDao extends BaseDao<TollStation> {

	@Override
	public TollStation getById(long id) {
		return super.getById(id, TollStation.class);
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, TollStation.class);
	}

	@Override
	public QueryResult<TollStation> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, TollStation.class);
	}
//	public static TollStation getByName(String name) {
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		Query query = session.createQuery("from TollStation tollStation where tollStation.name = :name");
//		query.setParameter("name", name);
//		@SuppressWarnings("unchecked")
//		List<TollStation> result = query.list();
//		session.getTransaction().commit();
//		if (result.size() == 0) {
//			return null;
//		} else {
//			return result.get(0);
//		}
//	}
//	
//	public static TollStation getById(String id) {
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		Query query = session.createQuery("from TollStation tollStation where tollStation.id = :id");
//		query.setParameter("id", id);
//		@SuppressWarnings("unchecked")
//		List<TollStation> result = query.list();
//		session.getTransaction().commit();
//		if (result.size() == 0) {
//			return null;
//		} else {
//			return result.get(0);
//		}
//	}
//	
//	public static boolean exists(String name) {
//		return getByName(name) != null;
//	}
//	
//	public static void add(String name, String description, double x, double y) {
//		TollStation tollStation = new TollStation();
//		tollStation.setName(name);
//		tollStation.setDescription(description);
//		tollStation.setX(x);
//		tollStation.setY(y);
//		
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		session.save(tollStation);
//
//		session.getTransaction().commit();
//	}
//	
//	public static JSONObject querySelect(int page, int pageSize, Map<String, Object> filter) throws JSONException, ChannyException  {
//		int match = UserDao.getCount(filter);
//
//		String hql = "from TollStation tollStation";
//		if (filter != null && filter.size() > 0) {
//			hql = HibernateUtil.assembleHql(filter);
//		}
//
//		int offset = page * pageSize;
//		if (offset >= match) {
//			offset = (match - 1) / pageSize * pageSize;
//		}
//
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		Query query = session.createQuery(hql);
//		HibernateUtil.assembleQuery(query, filter);
//		query.setFirstResult(offset);
//		query.setMaxResults(pageSize);
//
//		@SuppressWarnings("unchecked")
//		List<TollStation> list = query.list();
//		session.getTransaction().commit();
//		
//		JSONArray data = new JSONArray();
//		for (TollStation tollStation : list) {
//			JSONObject obj = new JSONObject();
//			obj.put("id", tollStation.getId());
//			obj.put("text", tollStation.getName());
//			data.put(obj);
//		}
//
//		JSONObject result = new JSONObject();
//		result.put("results", data);
//		result.put("total", match);
//		return result;
//	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(TollStation.class, filter);
	}
	
	public TollStation getByName(String name) {
		List<TollStation> result = super.getByField("name", name, TollStation.class);
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0); 
	}
	
//	public static void main(String[] args) throws JSONException, ChannyException {
////		TollStation ts = getByName("土华");
////		System.out.println(ts.getName());
//		Map<String, Object> filter = new HashMap<String, Object>();
//		filter.put(HibernateUtil.ClassKey, TollStation.class);
//		System.out.println(querySelect(0, 10, filter));
//	}
}
