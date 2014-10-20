package channy.transmanager.shaobao.data.user;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import channy.transmanager.shaobao.data.BaseDao;
import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.model.order.Order;
import channy.transmanager.shaobao.model.order.OrderStatus;
import channy.transmanager.shaobao.model.user.Driver;
import channy.transmanager.shaobao.model.user.UserStatus;
import channy.util.ChannyException;
import channy.util.HibernateUtil;

public class DriverDao extends BaseDao<Driver> {
	// public static void add(String employeeId, String name, String password,
	// String role) throws XPathExpressionException, SAXException, IOException,
	// ParserConfigurationException,
	// ChannyException {
	// Role r = RoleDao.getRoleByName(role);
	// if (r == null) {
	// throw new ChannyException(ErrorCode.OBJECT_NOT_EXISTED,
	// String.format("角色不存在: %s", role));
	// }
	//
	// if (UserDao.exists(employeeId)) {
	// throw new ChannyException(ErrorCode.USER_EXISTED);
	// }
	//
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	//
	// Driver driver = new Driver();
	// driver.setEmployeeId(employeeId);
	// driver.setName(name);
	// password = Sha1.encode(password);
	// driver.setPassword(password);
	// driver.setDateCreated(new Date());
	//
	// driver.setRole(r);
	// session.save(driver);
	//
	// session.getTransaction().commit();
	// }
	//
	// public static Driver getByName(String name) {
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query =
	// session.createQuery("from Driver driver where driver.name = :name");
	// query.setParameter("name", name);
	// @SuppressWarnings("unchecked")
	// List<Driver> result = query.list();
	// session.getTransaction().commit();
	// if (result.size() == 0) {
	// return null;
	// } else {
	// return result.get(0);
	// }
	// }
	//
	// public static Driver getById(long id) {
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query =
	// session.createQuery("from Driver driver where driver.id = :id");
	// query.setParameter("id", id);
	// @SuppressWarnings("unchecked")
	// List<Driver> result = query.list();
	// session.getTransaction().commit();
	// if (result.size() == 0) {
	// return null;
	// } else {
	// return result.get(0);
	// }
	// }
	//
	// public static Driver getByEmployeeId(String id) {
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query =
	// session.createQuery("from Driver driver where driver.employeeId = :employeeId");
	// query.setParameter("employeeId", id);
	// @SuppressWarnings("unchecked")
	// List<Driver> result = query.list();
	// session.getTransaction().commit();
	// if (result.size() == 0) {
	// return null;
	// } else {
	// return result.get(0);
	// }
	// }
	//
	// public static JSONObject query(int page, int pageSize, Map<String,
	// Object> filter) throws JSONException, ChannyException {
	// int total = UserDao.getCount(null);
	// int match = UserDao.getCount(filter);
	//
	// String hql = "from Driver driver";
	// String condition = "";
	// if (filter != null && filter.size() > 0) {
	// hql += " where";
	// condition = HibernateUtil.assembleHqlCondition(filter, "driver");
	// hql += condition;
	// }
	//
	// //hql += " order by user.dateCreated";
	//
	// System.out.println(hql);
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
	// List<Driver> list = query.list();
	// session.getTransaction().commit();
	//
	// JSONObject result = new JSONObject();
	// JSONArray data = new JSONArray();
	// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	// for (Driver user : list) {
	// JSONObject u = new JSONObject();
	// u.put("id", user.getId());
	// u.put("employeeId", user.getEmployeeId());
	// u.put("status", user.getStatus().toString());
	// u.put("role", user.getDbRole());
	// if (user.getDbRole().equals("超级用户")) {
	// u.put("editable", false);
	// } else {
	// u.put("editable", true);
	// }
	// u.put("isLocked", user.isLocked());
	// u.put("name", user.getName());
	// u.put("dateCreated", format.format(user.getDateCreated()));
	// data.put(u);
	// }
	//
	// result.put("total", total);
	// result.put("match", match);
	// result.put("data", data);
	//
	// return result;
	// }

	public static void main(String[] args) {
		// System.out.println(getById(5));
	}

	@Override
	public Driver getById(long id) {
		return super.getById(id, Driver.class);
	}

	@Override
	public boolean exists(long id) {
		return getById(id) != null;
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, Driver.class);
	}

	@Override
	public QueryResult<Driver> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, Driver.class);
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(Driver.class, filter);
	}

	public List<Order> sync(Driver driver) {
		String hql = String
				.format("from Order order where order.driver = :driver and order.status != :s0 and order.status != :s1 and order.status != :s2 and order.status != :s3");

		Session session = HibernateUtil.getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery(hql);
		query.setParameter("driver", driver);
		query.setParameter("s0", OrderStatus.ExpensesVerificationPending);
		query.setParameter("s1", OrderStatus.CargoVerificationPending);
		query.setParameter("s2", OrderStatus.ClearancePending);
		query.setParameter("s3", OrderStatus.Closed);
		@SuppressWarnings("unchecked")
		List<Order> result = query.list();
		for (Order order : result) {
			order.getDriver().getName();
			order.getTruck().getPlate();
			order.getClient().getName();
		}
		session.getTransaction().commit();

		return result;
	}
	
	public List<Driver> getAvailableDrivers() {
		Session session = HibernateUtil.getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery("from Driver driver where driver.status = :status order by driver.lastOrder desc");
		query.setParameter("status", UserStatus.Idle);
		@SuppressWarnings("unchecked")
		List<Driver> result = query.list();
		session.getTransaction().commit();
		return result;
	}
	
	public Driver scheduleDriver() {
		Session session = HibernateUtil.getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery("from Driver driver where driver.status = :status order by driver.lastOrder desc");
		query.setParameter("status", UserStatus.Idle);
		query.setFirstResult(0);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		List<Driver> result = query.list();
		session.getTransaction().commit();
		
		if (result.isEmpty()) {
			return null;
		}
		
		return result.get(0);
	}
}
