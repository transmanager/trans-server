package channy.transmanager.shaobao.service.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.data.user.RoleDao;
import channy.transmanager.shaobao.data.user.UserDao;
import channy.transmanager.shaobao.model.user.Clerk;
import channy.transmanager.shaobao.model.user.Driver;
import channy.transmanager.shaobao.model.user.Guard;
import channy.transmanager.shaobao.model.user.Oiler;
import channy.transmanager.shaobao.model.user.Role;
import channy.transmanager.shaobao.model.user.Scheduler;
import channy.transmanager.shaobao.model.user.User;
import channy.transmanager.shaobao.model.user.UserStatus;
import channy.transmanager.shaobao.service.ServiceInterface;
import channy.util.ChannyException;
import channy.util.ErrorCode;
import channy.util.Sha1;

public class UserService implements ServiceInterface<User> {
	private UserDao dao = new UserDao();

	public JSONObject add(String employeeId, String name, String password, String role) throws ChannyException, XPathExpressionException,
			SAXException, IOException, ParserConfigurationException, JSONException {
		RoleDao d = new RoleDao();
		// Role r = RoleDao.getRoleByName(role);
		Role r = d.getByName(role);
		if (r == null) {
			throw new ChannyException(ErrorCode.OBJECT_NOT_EXISTED, String.format("角色不存在: %s", role));
		}
		
		if (dao.getByEmployeeId(employeeId) != null) {
			throw new ChannyException(ErrorCode.USER_EXISTED);
		}

		User user = new User();
		user.setEmployeeId(employeeId);
		user.setName(name);
		user.setPassword(Sha1.encode(password));
		user.setRole(r);
		// user.setDbRole(role);
		dao.add(user);

		int total = getCount();
		int page = (total - 1) / 15;
		JSONObject data = new JSONObject();
		data.put("page", page);

		return data;
	}

	@SuppressWarnings("unchecked")
	protected <T> T add(String employeeId, String name, String password, String role, Class<? extends User> clazz)
			throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException,
			InvocationTargetException, XPathExpressionException, SAXException, IOException, ParserConfigurationException, ChannyException {
		// Role r = RoleDao.getRoleByName(role);
		RoleDao d = new RoleDao();
		Role r = d.getByName(role);
		if (r == null) {
			throw new ChannyException(ErrorCode.OBJECT_NOT_EXISTED, String.format("角色不存在: %s", role));
		}

		UserDao dao = new UserDao();
		if (dao.getByEmployeeId(employeeId) != null) {
			throw new ChannyException(ErrorCode.USER_EXISTED);
		}

		// Method setEmployeeId = clazz.getMethod("setEmployeeId",
		// String.class);
		// Method setName = clazz.getMethod("setName", String.class);
		// Method setPassword = clazz.getMethod("setPassword", String.class);
		// Method setDbRole = clazz.getMethod("setDbRole", String.class);

		User entity = clazz.newInstance();
		entity.setEmployeeId(employeeId);
		entity.setName(name);
		entity.setPassword(Sha1.encode(password));
		entity.setRole(r);
		
		if (clazz == Driver.class) {
			entity.setStatus(UserStatus.Idle);
		}
		// entity.setDbRole(role);
		// setEmployeeId.invoke(entity, employeeId);
		// setName.invoke(entity);
		// setPassword.invoke(entity, password);
		// setDbRole.invoke(entity, role);

		dao.add(entity);
		return (T) entity;
	}

	
	public User getById(long id) {
		return dao.getById(id);
	}

	public User getByEmployeeId(String id) {
		return dao.getByEmployeeId(id);
	}

	
	public void remove(User user) {

		dao.remove(user);
	}

	
	public void removeById(long id) throws ChannyException {
		dao.removeById(id);
	}

	
	public void update(User user) {
		dao.update(user);
	}

	
	public JSONObject query(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException {
		QueryResult<User> result = dao.query(page, pageSize, filter);

		List<User> list = result.getData();

		JSONObject data = new JSONObject();
		data.put("total", result.getTotal());
		data.put("match", result.getMatch());

		JSONArray array = new JSONArray();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (User user : list) {
			JSONObject u = new JSONObject();
			u.put("id", user.getId());
			u.put("employeeId", user.getEmployeeId());
			u.put("status", user.getStatus().toString());
			if (user.getRole() == null) {
				continue;
			}
			u.put("role", user.getRole().getName());
			u.put("editable", user.getRole().isEditable());
			u.put("isLocked", user.isLocked());
			u.put("name", user.getName());
			u.put("dateCreated", format.format(user.getDateCreated()));
			array.put(u);
		}

		data.put("data", array);

		return data;
	}

	
	public JSONObject select(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException {
		QueryResult<User> result = dao.query(page, pageSize, filter);
		List<User> list = result.getData();

		JSONObject obj = new JSONObject();
		obj.put("total", result.getMatch());

		JSONArray array = new JSONArray();
		for (User user : list) {
			JSONObject u = new JSONObject();
			u.put("id", user.getId());
			// u.put("employeeId", user.getEmployeeId());
			u.put("text", user.getName());
			array.put(u);
		}
		obj.put("data", array);

		return obj;
	}

	
	public boolean exists(long id) {
		return getById(id) != null;
	}

	public void lock(long id) throws ChannyException {
		if (!exists(id)) {
			throw new ChannyException(ErrorCode.USER_NOT_EXISTED);
		}
		User user = getById(id);
		user.setLocked(true);
		update(user);
	}

	public void unlock(long id) throws ChannyException {
		if (!exists(id)) {
			throw new ChannyException(ErrorCode.USER_NOT_EXISTED);
		}
		User user = getById(id);
		user.setLocked(false);
		update(user);
	}

	
	public int getCount() throws ChannyException {

		return dao.getCount(null);
	}

	public void changePassword(long id, String oldPassword, String newPassword) throws ChannyException {
		if (!exists(id)) {
			throw new ChannyException(ErrorCode.USER_NOT_EXISTED);
		}
		User user = getById(id);
		ErrorCode code = auth(user.getEmployeeId(), oldPassword);
		if (!code.isOK()) {
			throw new ChannyException(code);
		}

		user.setPassword(Sha1.encode(newPassword));
		update(user);
	}

	public ErrorCode auth(String employeeId, String password) {
		User user = getByEmployeeId(employeeId);
		if (user == null) {
			return ErrorCode.USER_NOT_EXISTED;
		}

		if (!user.getPassword().equals(Sha1.encode(password))) {
			return ErrorCode.INCORRECT_PASSWORD;
		}

		return ErrorCode.OK;
	}
	
	public User getDetailByEmployeeId(String employeeId, Session session) {
		User user = dao.getDetailByEmployeeId(employeeId, session);
		user.getRole().getName();
		//System.out.println(user.getRole().getName());
		return user;
	}

	public void importUser(String fileName) throws XPathExpressionException, SAXException, ParserConfigurationException, ChannyException,
			SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException,
			InvocationTargetException, JSONException {
		File file = new File(fileName);
		BufferedReader reader = null;
		UserService service = new UserService();
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("\t");
				// for (String part : parts) {
				// System.out.println(part);
				// }
				System.out.println(line);
				if (parts[2].equals("长途车驾驶员") || parts[2].equals("中转车驾驶员")) {
					service.add(parts[1], parts[0], "888888", parts[2], Driver.class);
					// DriverService.add(parts[1], parts[0], "888888",
					// parts[2]);
				} else if (parts[2].contains("调度")) {
					service.add(parts[1], parts[0], "888888", parts[2], Scheduler.class);
				} else if (parts[2].contains("文员")) {
					service.add(parts[1], parts[0], "888888", parts[2], Clerk.class);
				} else if (parts[2].contains("加油工")) {
					service.add(parts[1], parts[0], "888888", parts[2], Oiler.class);
				} else if (parts[2].contains("门卫")) {
					service.add(parts[1], parts[0], "888888", parts[2], Guard.class);
				} else {
					add(parts[1], parts[0], "888888", parts[2]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public List<User> getByName(String name) {
		return dao.getByName(name);
	}

	public static void main(String[] args) throws XPathExpressionException, SAXException, ParserConfigurationException, ChannyException, IOException,
			SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException,
			InvocationTargetException, JSONException {
		UserService service = new UserService();
		service.add("admin", "admin", "admin", "超级用户");
		service.importUser("/Users/Channy/Desktop/Channy's/shaobao/names");
		
//		User user = service.getById(1);
//		Set<Module> modules = user.getGrantedModules();
//		for (Module module : modules) {
//			System.out.println(module.getDescription());
//		}
		
//		Session session = HibernateUtil.getCurrentSession();
//		session.beginTransaction();
//		User user = service.getDetailByEmployeeId("admin", session);
//		session.getTransaction().commit();
//		System.out.println(user.getRole().getName());
		
		//System.out.println(service.auth("admin", "admin").getDetail());
	}
	
}
