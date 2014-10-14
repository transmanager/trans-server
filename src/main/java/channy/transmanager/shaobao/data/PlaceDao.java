package channy.transmanager.shaobao.data;

import java.util.Map;

import channy.transmanager.shaobao.model.Place;
import channy.util.ChannyException;

public class PlaceDao extends BaseDao<Place> {
//	public static Place getByName(String name) {
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		Query query = session.createQuery("from Place place where place.name = :name");
//		query.setParameter("name", name);
//		@SuppressWarnings("unchecked")
//		List<Place> result = query.list();
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
//	public static void add(String name, String description) {
//		Place place = new Place();
//		place.setName(name);
//		place.setDescription(description);
//		
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		session.save(place);
//
//		session.getTransaction().commit();
//	}
//	
//	public static int getCount(Map<String, Object> filter) throws ChannyException {
//		String hql = "select count(*) from Place place";
//		if (filter != null && filter.size() > 0) {
//			hql += " where";
//			String condition = HibernateUtil.assembleHqlCondition(filter, "place");
//			hql += condition;
//		}
//
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		Query query = session.createQuery(hql);
//		HibernateUtil.assembleQuery(query, filter);
//		@SuppressWarnings("rawtypes")
//		List list = query.list();
//		session.getTransaction().commit();
//
//		Integer count = Integer.parseInt(list.get(0).toString());
//		return count;
//	}
//	
//	public static JSONObject querySelect(int page, int pageSize, Map<String, Object> filter) throws JSONException, ChannyException  {
//		int match = PlaceDao.getCount(filter);
//
//		String hql = "from Place place";
//		String condition = "";
//		if (filter != null && filter.size() > 0) {
//			hql += " where";
//			condition = HibernateUtil.assembleHqlCondition(filter, "place");
//			hql += condition;
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
//		List<Place> list = query.list();
//		session.getTransaction().commit();
//		
//		JSONArray data = new JSONArray();
//		for (Place place : list) {
//			JSONObject obj = new JSONObject();
//			obj.put("id", place.getId());
//			obj.put("text", place.getName());
//			data.put(obj);
//		}
//
//		JSONObject result = new JSONObject();
//		result.put("results", data);
//		result.put("total", match);
//		return result;
//	}
//	
//	public static void main(String[] args) throws JSONException, ChannyException {
//		Map<String, Object> filter = new HashMap<String, Object>();
//		System.out.println(getCount(filter));
//		System.out.println(querySelect(0, 10, filter));
//	}

	@Override
	public Place getById(long id) {
		return super.getById(id, Place.class);
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, Place.class);
	}

	@Override
	public QueryResult<Place> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, Place.class);
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(Place.class, filter);
	}
	
	public Place getByName(String name) {
		return (Place) super.getByField("name", name, Place.class);
	}
}
