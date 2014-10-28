package channy.transmanager.shaobao.data;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import channy.util.ChannyException;
import channy.util.ErrorCode;
import channy.util.HibernateUtil;

public abstract class BaseDao<T> {
	public abstract T getById(long id);

	public boolean exists(long id) {
		return getById(id) != null;
	}

	public abstract void removeById(long id) throws ChannyException;

	public abstract QueryResult<T> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException;

	public abstract int getCount(Map<String, Object> filter) throws ChannyException;

	protected T getById(long id, Class<T> clazz) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(String.format("from %s entity where entity.id = :id", clazz.getSimpleName()));
			query.setParameter("id", id);
			@SuppressWarnings("unchecked")
			List<T> result = query.list();

			if (result.size() == 0) {
				return null;
			} else {
				return result.get(0);
			}
		} finally {
			session.getTransaction().commit();
		}
	}

	/**
	 * Used for lazy fetching, but the caller must manipulate transaction by
	 * self.
	 * 
	 * @param id
	 * @param session
	 * @param clazz
	 * @return
	 */
	protected T getById(long id, Session session, Class<T> clazz) {
		Query query = session.createQuery(String.format("from %s entity where entity.id = :id", clazz.getSimpleName()));
		query.setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<T> result = query.list();
		if (result.size() == 0) {
			return null;
		} else {
			return result.get(0);
		}
	}

	protected void removeById(long id, Class<T> clazz) throws ChannyException {
		T entity = getById(id, clazz);
		if (entity == null) {
			throw new ChannyException(ErrorCode.OBJECT_NOT_EXISTED);
		}

		remove(entity);
	}

	public void add(T entity) {
		HibernateUtil.persist(entity);
	}

	protected QueryResult<T> query(int page, int pageSize, Map<String, Object> filter, Class<T> clazz) throws ChannyException {
		int total = getCount(clazz, null);

		if (filter != null) {
			filter.put(HibernateUtil.ClassKey, clazz);
		}
		int match = getCount(clazz, filter);
		String hql = HibernateUtil.assembleHql(filter);

		int offset = page * pageSize;
		if (offset >= match) {
			offset = (match - 1) / pageSize * pageSize;
		}

		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(hql);
			HibernateUtil.assembleQuery(query, filter);
			query.setFirstResult(offset);
			query.setMaxResults(pageSize);

			@SuppressWarnings("unchecked")
			List<T> list = query.list();

			QueryResult<T> result = new QueryResult<T>();
			result.setTotal(total);
			result.setMatch(match);
			result.setData(list);
			return result;
		} finally {
			session.getTransaction().commit();
		}
	}

	/**
	 * Used for lazy fetching, but the caller must manipulate transaction by
	 * self.
	 * 
	 * @param page
	 * @param pageSize
	 * @param filter
	 * @param session
	 * @param clazz
	 * @return
	 * @throws ChannyException
	 */
	protected QueryResult<T> query(int page, int pageSize, Map<String, Object> filter, Session session, Class<T> clazz) throws ChannyException {
		int total = getCount(clazz, session, null);

		if (filter != null) {
			filter.put(HibernateUtil.ClassKey, clazz);
		}
		int match = getCount(clazz, session, filter);
		String hql = HibernateUtil.assembleHql(filter);

		int offset = page * pageSize;
		if (offset >= match) {
			offset = (match - 1) / pageSize * pageSize;
		}

		// Session session = HibernateUtil.getCurrentSession();
		// session.beginTransaction();
		Query query = session.createQuery(hql);
		HibernateUtil.assembleQuery(query, filter);
		query.setFirstResult(offset);
		query.setMaxResults(pageSize);

		@SuppressWarnings("unchecked")
		List<T> list = query.list();
		// session.getTransaction().commit();

		QueryResult<T> result = new QueryResult<T>();
		result.setTotal(total);
		result.setMatch(match);
		result.setData(list);
		return result;
	}

	public void update(T entity) {
		HibernateUtil.update(entity);
	}

	public void remove(T entity) {
		HibernateUtil.remove(entity);
	}

	protected List<T> getByField(String field, String value, Class<T> clazz) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(String.format("from %s entity where entity.%s = :%s", clazz.getSimpleName(), field, field));
			query.setParameter(field, value);
			@SuppressWarnings("unchecked")
			List<T> result = query.list();
			if (result.size() == 0) {
				return null;
			} else {
				return result;
			}
		} finally {
			session.getTransaction().commit();
		}
	}

	protected List<T> getByField(String field, String value, Session session, Class<T> clazz) {
		// Session session = HibernateUtil.getCurrentSession();
		// session.beginTransaction();
		Query query = session.createQuery(String.format("from %s entity where entity.%s = :%s", clazz.getSimpleName(), field, field));
		query.setParameter(field, value);
		@SuppressWarnings("unchecked")
		List<T> result = query.list();
		// session.getTransaction().commit();

		if (result.size() == 0) {
			return null;
		} else {
			return result;
		}
	}

	protected int getCount(Class<T> clazz, Map<String, Object> filter) throws ChannyException {
		String hql = String.format("select count(*) from %s entity", clazz.getSimpleName());
		if (filter != null && filter.size() > 0) {
			hql = String.format("select count(*) %s", HibernateUtil.assembleHql(filter));
		}

		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(hql);
			HibernateUtil.assembleQuery(query, filter);
			@SuppressWarnings("rawtypes")
			List list = query.list();

			Integer count = Integer.parseInt(list.get(0).toString());
			return count;
		} finally {
			session.getTransaction().commit();
		}
	}

	protected int getCount(Class<T> clazz, Session session, Map<String, Object> filter) throws ChannyException {
		String hql = String.format("select count(*) from %s entity", clazz.getSimpleName());
		if (filter != null && filter.size() > 0) {
			hql = String.format("select count(*) %s", HibernateUtil.assembleHql(filter));
		}

		// Session session = HibernateUtil.getCurrentSession();
		// session.beginTransaction();
		Query query = session.createQuery(hql);
		HibernateUtil.assembleQuery(query, filter);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		// session.getTransaction().commit();

		Integer count = Integer.parseInt(list.get(0).toString());
		return count;
	}
}
