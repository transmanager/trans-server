package channy.transmanager.shaobao.data.user;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONException;
import org.xml.sax.SAXException;

import channy.util.ChannyException;
import channy.transmanager.shaobao.data.BaseDao;
import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.model.user.User;

public class UserDao extends BaseDao<User> {
	// public static void add(String employeeId, String name, String password,
	// String role) throws XPathExpressionException, SAXException, IOException,
	// ParserConfigurationException, ChannyException {
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
	// User user = new User();
	// user.setEmployeeId(employeeId);
	// user.setName(name);
	// password = Sha1.encode(password);
	// user.setPassword(password);
	// user.setDateCreated(new Date());
	//
	// user.setRole(r);
	// session.save(user);
	//
	// session.getTransaction().commit();
	// }
	//
	// public static boolean exists(String employeeId) {
	// return (UserDao.getByEmployeeId(employeeId) != null);
	// }
	//
	// public static User getByEmployeeId(String id) {
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query =
	// session.createQuery("from User user where user.employeeId = :employeeId");
	// query.setParameter("employeeId", id);
	// @SuppressWarnings("unchecked")
	// List<User> result = query.list();
	// session.getTransaction().commit();
	// if (result.size() == 0) {
	// return null;
	// } else {
	// return result.get(0);
	// }
	// }
	//
	// public static User getByName(String name) {
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query =
	// session.createQuery("from User user where user.name = :name");
	// query.setParameter("name", name);
	// @SuppressWarnings("unchecked")
	// List<User> result = query.list();
	// session.getTransaction().commit();
	// if (result.size() == 0) {
	// return null;
	// } else {
	// return result.get(0);
	// }
	// }
	//
	// public static User getById(long id) {
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query = session.createQuery("from User user where user.id = :id");
	// query.setParameter("id", id);
	// @SuppressWarnings("unchecked")
	// List<User> result = query.list();
	// session.getTransaction().commit();
	// if (result.size() == 0) {
	// return null;
	// } else {
	// return result.get(0);
	// }
	// }
	//
	// public static ErrorCode authenticate(String employeeId, String password)
	// {
	// User user = UserDao.getByEmployeeId(employeeId);
	// if (user == null) {
	// return ErrorCode.USER_NOT_EXISTED;
	// }
	//
	// System.out.println(user.getPassword());
	// System.out.println(password);
	// System.out.println(Sha1.encode(password));
	// if (!user.getPassword().equals(Sha1.encode(password))) {
	// return ErrorCode.INCORRECT_PASSWORD;
	// }
	//
	// return ErrorCode.OK;
	// }
	//
	// public static int getCount(Map<String, Object> filter) throws
	// ChannyException {
	// String hql = "select count(*) from User user";
	// if (filter != null && filter.size() > 0) {
	// // hql += " where";
	// // String condition = HibernateUtil.assembleHqlCondition(filter,
	// // "user");
	// // hql += condition;
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
	// public static JSONObject querySelect(int page, int pageSize, Map<String,
	// Object> filter) throws JSONException, ChannyException {
	// int match = UserDao.getCount(filter);
	//
	// // String hql = "from User user";
	// // String condition = "";
	// // if (filter != null && filter.size() > 0) {
	// // hql += " where";
	// // condition = HibernateUtil.assembleHqlCondition(filter, "user");
	// // hql += condition;
	// // }
	//
	// // System.out.println(condition);
	//
	// String hql = HibernateUtil.assembleHql(filter);
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
	// List<User> list = query.list();
	// session.getTransaction().commit();
	//
	// JSONArray data = new JSONArray();
	// for (User user : list) {
	// JSONObject obj = new JSONObject();
	// obj.put("id", user.getId());
	// obj.put("text", user.getName());
	// data.put(obj);
	// }
	//
	// JSONObject result = new JSONObject();
	// result.put("results", data);
	// result.put("total", match);
	// return result;
	// }
	//
	// public static JSONObject query(int page, int pageSize, Map<String,
	// Object> filter) throws JSONException, ChannyException {
	// int total = UserDao.getCount(null);
	// int match = UserDao.getCount(filter);
	//
	// // String hql = "from User user";
	// // String condition = "";
	// // if (filter != null && filter.size() > 0) {
	// // hql += " where";
	// // condition = HibernateUtil.assembleHqlCondition(filter, "user");
	// // hql += condition;
	// // }
	//
	// String hql = HibernateUtil.assembleHql(filter);
	//
	// // hql += " order by user.dateCreated";
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
	// List<User> list = query.list();
	// session.getTransaction().commit();
	//
	// JSONObject result = new JSONObject();
	// JSONArray data = new JSONArray();
	// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	// for (User user : list) {
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
	//
	// public static void remove(long id) throws ChannyException {
	// User user = UserDao.getById(id);
	// if (user == null) {
	// throw new ChannyException(ErrorCode.USER_NOT_EXISTED);
	// }
	//
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// session.delete(user);
	// session.getTransaction().commit();
	// }
	//
	// public static void importUser(String fileName) throws
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
	// if (parts[2].equals("长途车驾驶员") || parts[2].equals("中转车驾驶员")) {
	// DriverDao.add(parts[1], parts[0], "888888", parts[2]);
	// } else {
	// add(parts[1], parts[0], "888888", parts[2]);
	// }
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
	//
	// public static void update(User user) {
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// session.update(user);
	// session.getTransaction().commit();
	// }
	//
	// public static void lock(Long id) throws ChannyException {
	// User user = UserDao.getById(id);
	// if (user == null) {
	// throw new ChannyException(ErrorCode.USER_NOT_EXISTED);
	// }
	//
	// user.setLocked(true);
	// update(user);
	// }
	//
	// public static void unlock(Long id) throws ChannyException {
	// User user = UserDao.getById(id);
	// if (user == null) {
	// throw new ChannyException(ErrorCode.USER_NOT_EXISTED);
	// }
	//
	// user.setLocked(false);
	// update(user);
	// }

	public static void main(String[] args) throws JSONException, XPathExpressionException, SAXException, IOException, ParserConfigurationException,
			ChannyException {
		// User user = UserDao.getUserById("hqf");
		// System.out.println(user.getName());

		// UserDao.add("admin", "袁俊", "admin", "超级用户");
		// for (int i = 0; i < 40; i++) {
		// UserDao.add("admin" + i, "袁俊", "admin", "超级用户");
		// }
		// System.out.println(UserDao.authenticate("admin", "admin"));
		// UserDao.remove("admin");

		// Map<String, Object> filter = new HashMap<String, Object>();
		// filter.put("employeeId", "10");
		// System.out.println(UserDao.query(0, 15, filter));

		UserDao dao = new UserDao();
		// User user = new User();
		// user.setName("admin");
		// user.setPassword(Sha1.encode("admin"));
		// user.setEmployeeId("admin");
		// user.setDbRole("超级用户");
		// //// dao.add("admin", "admin", "admin", "超级用户");
		// dao.add(user);

		// importUser("/Users/Channy/Desktop/Channy's/shaobao/names");

//		Map<String, Object> filter = new HashMap<String, Object>();
//		filter.put(HibernateUtil.ClassKey, User.class);
//		filter.put("dbRole", "驾驶员");
//		// filter.put("status", UserStatus.Normal);
//		dao.query(0, 15, filter);
		System.out.println(dao.getByField("name", "admin", User.class).size());
		// System.out.println(querySelect(0, 10, filter));
	}

	@Override
	public User getById(long id) {
		return super.getById(id, User.class);
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, User.class);
	}

	@Override
	public QueryResult<User> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, User.class);
	}
	
	public User getByEmployeeId(String employeeId) {
		List<User> result = super.getByField("employeeId", employeeId, User.class);
		if (result == null) {
			return null;
		}
		
		return result.get(0);
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(User.class, filter);
	}
	
	public List<User> getByName(String name) {
		return super.getByField("name", name, User.class);
	}
}
