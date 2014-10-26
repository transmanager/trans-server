package channy.util;

import java.util.Date;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	public static final String ClassKey = "efd8f6f7eb1c36b5";
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			Configuration config = new Configuration().configure();
			StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
			return config.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public static Session openSession() {
		return sessionFactory.openSession();
	}

	public static void closeSession(Session session) {
		session.close();
	}

	public static <T> void persist(T entity) {
		// Session session = HibernateUtil.openSession();
		// try {
		// session.beginTransaction();
		// session.save(entity);
		// session.getTransaction().commit();
		// } finally {
		// session.close();
		// }

		Session session = HibernateUtil.getCurrentSession();
		session.beginTransaction();
		session.save(entity);
		session.getTransaction().commit();
	}

	public static <T> void update(T entity) {
		// Session session = HibernateUtil.openSession();
		// try {
		// session.beginTransaction();
		// session.update(entity);
		// session.getTransaction().commit();
		// } finally {
		// session.close();
		// }

		Session session = HibernateUtil.getCurrentSession();
		session.beginTransaction();
		session.update(entity);
		session.getTransaction().commit();
	}

	public static <T> void remove(T entity) {
		// Session session = HibernateUtil.openSession();
		// try {
		// session.beginTransaction();
		// session.delete(entity);
		// session.getTransaction().commit();
		// } finally {
		// session.close();
		// }

		Session session = HibernateUtil.getCurrentSession();
		session.beginTransaction();
		session.delete(entity);
		session.getTransaction().commit();
	}

	// private static SessionFactory getSessionFactory() {
	// return sessionFactory;
	// }

	private void close() {
		sessionFactory.close();
	}

	@Override
	protected void finalize() throws Throwable {
		close();
	};

	public static String assembleHqlCondition(Map<String, Object> filter, String objectName) {
		String condition = "";
		for (String key : filter.keySet()) {
			if (key.equals(HibernateUtil.ClassKey)) {
				continue;
			}
			if (!condition.equals("")) {
				condition += " and";
			}

			if (key.contains("date")) {
				String date = (String) filter.get(key);
				if (date.startsWith(">=")) {
					date = date.substring(2);
					condition += String.format(" %s.%s >= :%s", objectName, key, String.format("%s_from", objectName));
				} else if (date.startsWith("<=")) {
					date = date.substring(2);
					condition += String.format(" %s.%s <= :%s", objectName, key, String.format("%s_to", objectName));
				} else if (date.contains(" - ")) {
					condition += String.format(" %s.%s >= :%s", objectName, key, String.format("%s_from", objectName));
					condition += String.format(" and %s.%s <= :%s", objectName, key, String.format("%s_to", objectName));
				}
			} else {
				// System.out.println(filter.get(key).getClass().getName());
				Object term = filter.get(key);
				if (term == null) {
					condition += String.format(" %s.%s is null", objectName, key);
				} else {
					if (term.getClass().getName().equals("java.lang.String")) {
						if (term != null) {
							condition += String.format(" %s.%s like :%s", objectName, key, String.format("%s_%s", objectName, key));
						}
					} else {
						condition += String.format(" %s.%s = :%s", objectName, key, String.format("%s_%s", objectName, key));
					}
				}
			}
		}

		return condition;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String assembleHql(Map<String, Object> filter) throws ChannyException {
		if (filter == null) {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, "无效的Filter");
		}
		if (filter != null && !filter.containsKey(ClassKey)) {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, "找不到ClassKey");
		}

		Class clazz = (Class) filter.get(ClassKey);

		String objectName = clazz.getSimpleName().toLowerCase();
		String hql = String.format("from %s %s", clazz.getSimpleName(), objectName);
		String condition = "";
		for (String key : filter.keySet()) {
			if (key.equals(ClassKey)) {
				continue;
			}

			if (!condition.equals("")) {
				condition += " and";
			}

			if (key.contains("date")) {
				String date = (String) filter.get(key);
				if (date.startsWith(">=")) {
					date = date.substring(2);
					condition += String.format(" %s.%s >= :%s", objectName, key, String.format("%s_from", objectName));
				} else if (date.startsWith("<=")) {
					date = date.substring(2);
					condition += String.format(" %s.%s <= :%s", objectName, key, String.format("%s_to", objectName));
				} else if (date.contains(" - ")) {
					condition += String.format(" %s.%s >= :%s", objectName, key, String.format("%s_from", objectName));
					condition += String.format(" and %s.%s <= :%s", objectName, key, String.format("%s_to", objectName));
				}
			} else {
				// System.out.println(filter.get(key).getClass().getName());
				Object term = filter.get(key);
				if (term == null) {
					condition += String.format(" %s.%s is null", objectName, key);
				} else {
					if (term.getClass().getName().equals("java.lang.String")) {
						if (term != null) {
							condition += String.format(" %s.%s like :%s", objectName, key, String.format("%s_%s", objectName, key));
						}
					} else if (term.getClass().getName().equals("java.util.HashMap")) {
						Map<String, Object> nestedFilter = (Map<String, Object>) filter.get(key);
						String nestedHql = assembleHql(nestedFilter);
						condition += String.format(" %s.%s in (%s)", objectName, key, nestedHql);
					} else {
						condition += String.format(" %s.%s = :%s", objectName, key, String.format("%s_%s", objectName, key));
					}
				}
			}
		}

		if (!condition.equals("")) {
			hql += String.format(" where %s", condition);
		}
		return hql;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void assembleQuery(Query query, Map<String, Object> filter) throws ChannyException {
		if (filter != null) {
			if (!filter.containsKey(ClassKey)) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, "找不到ClassKey");
			}

			Class clazz = (Class) filter.get(ClassKey);
			String objectName = clazz.getSimpleName().toLowerCase();

			for (String key : filter.keySet()) {
				if (key.equals(ClassKey) || filter.get(key) == null) {
					continue;
				}

				if (key.contains("date")) {
					String date = (String) filter.get(key);
					if (date.startsWith(">=")) {
						date = date.substring(2);
						Date from = new Date(Long.parseLong(date));
						query.setParameter(String.format("%s_from", objectName), from);
						// condition += String.format(" %s.%s >= :%s",
						// objectName, key, String.format("%s_from",
						// objectName));
					} else if (date.startsWith("<=")) {
						date = date.substring(2);
						Date to = new Date(Long.parseLong(date));
						query.setParameter(String.format("%s_to", objectName), to);
						// condition += String.format(" %s.%s <= :%s",
						// objectName, key, String.format("%s_to", objectName));
					} else if (date.contains(" - ")) {
						// condition += String.format(" %s.%s >= :%s",
						// objectName, key, String.format("%s_from",
						// objectName));
						// condition += String.format(" and %s.%s <= :%s",
						// objectName, key, String.format("%s_to", objectName));
						String[] parts = date.split(" - ");
						Date from = new Date(Long.parseLong(parts[0]));
						Date to = new Date(Long.parseLong(parts[1]));
						query.setParameter(String.format("%s_from", objectName), from);
						query.setParameter(String.format("%s_to", objectName), to);
					}
				} else {
					Object term = filter.get(key);
					if (term.getClass().getName().equals("java.lang.String")) {
						query.setParameter(String.format("%s_%s", objectName, key), String.format("%%%s%%", filter.get(key)));
					} else if (term.getClass().getName().equals("java.util.HashMap")) {
						Map<String, Object> nestedFilter = (Map<String, Object>) filter.get(key);
						assembleQuery(query, nestedFilter);
					} else {
						query.setParameter(String.format("%s_%s", objectName, key), filter.get(key));
					}
				}
			}
		}
	}
}
