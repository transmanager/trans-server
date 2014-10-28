package channy.transmanager.shaobao.data.user;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.hibernate.Session;
import org.json.JSONException;
import org.xml.sax.SAXException;

import channy.util.ChannyException;
import channy.util.HibernateUtil;
import channy.transmanager.shaobao.data.BaseDao;
import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.model.user.User;

public class UserDao extends BaseDao<User> {
	public static void main(String[] args) throws JSONException, XPathExpressionException, SAXException, IOException, ParserConfigurationException,
			ChannyException {
		UserDao dao = new UserDao();
		System.out.println(dao.getByField("name", "admin", User.class).size());
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
		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			QueryResult<User> result = super.query(page, pageSize, filter, session, User.class);
			for (User user : result.getData()) {
				user.getRole().getName();
			}
			return result;
		} finally {
			session.getTransaction().commit();
		}
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

	public User getDetailByEmployeeId(String employeeId, Session session) {
		List<User> result = super.getByField("employeeId", employeeId, session, User.class);
		if (result == null) {
			return null;
		}

		return result.get(0);
	}
}
