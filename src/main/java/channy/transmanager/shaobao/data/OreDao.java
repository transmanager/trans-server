package channy.transmanager.shaobao.data;

import java.util.List;
import java.util.Map;

import channy.transmanager.shaobao.model.Ore;
import channy.util.ChannyException;

public class OreDao extends BaseDao<Ore> {
	public Ore getByName(String name) {
		List<Ore> ores =  super.getByField("name", name, Ore.class);
		if (ores.isEmpty()) {
			return null;
		}
		
		return ores.get(0);
	}
	
//	public static boolean exists(String name) {
//		return getByName(name) != null;
//	}
//	
//	public static void add(String name, String description) {
//		Ore ore = new Ore();
//		ore.setName(name);
//		ore.setDescription(description);
//		
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		session.save(ore);
//
//		session.getTransaction().commit();
//	}
//	
//	public static int getCount(Map<String, Object> filter) throws ChannyException {
//		String hql = "select count(*) from Ore ore";
//		if (filter != null && filter.size() > 0) {
//			hql += " where";
//			String condition = HibernateUtil.assembleHqlCondition(filter, "ore");
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
//		int match = OreDao.getCount(filter);
//
//		String hql = "from Ore ore";
//		String condition = "";
//		if (filter != null && filter.size() > 0) {
//			hql += " where";
//			condition = HibernateUtil.assembleHqlCondition(filter, "ore");
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
//		List<Ore> list = query.list();
//		session.getTransaction().commit();
//		
//		JSONArray data = new JSONArray();
//		for (Ore ore : list) {
//			JSONObject obj = new JSONObject();
//			obj.put("id", ore.getId());
//			obj.put("text", ore.getName());
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
	public Ore getById(long id) {
		return super.getById(id, Ore.class);
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, Ore.class);
	}

	@Override
	public QueryResult<Ore> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, Ore.class);
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(Ore.class, filter);
	}
}
