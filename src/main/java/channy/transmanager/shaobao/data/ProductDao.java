package channy.transmanager.shaobao.data;

import java.util.List;
import java.util.Map;

import channy.transmanager.shaobao.model.Product;
import channy.util.ChannyException;

public class ProductDao extends BaseDao<Product> {
	public Product getByName(String name) {
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		Query query = session.createQuery("from Product product where product.name = :name");
//		query.setParameter("name", name);
//		@SuppressWarnings("unchecked")
//		List<Product> result = query.list();
//		session.getTransaction().commit();
//		if (result.size() == 0) {
//			return null;
//		} else {
//			return result.get(0);
//		}
		List<Product> result = super.getByField("name", name, Product.class);
		if (result != null) {
			return result.get(0);
		}
		
		return null;
	}

	@Override
	public Product getById(long id) {
		return super.getById(id, Product.class);
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, Product.class);
	}

	@Override
	public QueryResult<Product> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, Product.class);
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(Product.class, filter);
	}
	
//	public static boolean exists(String name) {
//		return getByName(name) != null;
//	}
//	
//	public static void add(String name, String description) {
//		Product product = new Product();
//		product.setName(name);
//		product.setDescription(description);
//		
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		session.save(product);
//
//		session.getTransaction().commit();
//	}
//	
//	public static int getCount(Map<String, Object> filter) throws ChannyException {
//		String hql = "select count(*) from Product product";
//		if (filter != null && filter.size() > 0) {
//			hql += " where";
//			String condition = HibernateUtil.assembleHqlCondition(filter, "product");
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
//		int match = ProductDao.getCount(filter);
//
//		String hql = "from Product product";
//		String condition = "";
//		if (filter != null && filter.size() > 0) {
//			hql += " where";
//			condition = HibernateUtil.assembleHqlCondition(filter, "product");
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
//		List<Product> list = query.list();
//		session.getTransaction().commit();
//		
//		JSONArray data = new JSONArray();
//		for (Product product : list) {
//			JSONObject obj = new JSONObject();
//			obj.put("id", product.getId());
//			obj.put("text", product.getName());
//			data.put(obj);
//		}
//
//		JSONObject result = new JSONObject();
//		result.put("results", data);
//		result.put("total", match);
//		return result;
//	}
}
