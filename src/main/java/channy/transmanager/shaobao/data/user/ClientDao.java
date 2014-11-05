package channy.transmanager.shaobao.data.user;

import java.util.List;
import java.util.Map;

import channy.transmanager.shaobao.data.BaseDao;
import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.model.user.Client;
import channy.util.ChannyException;

public class ClientDao extends BaseDao<Client> {
//	public static void add(String employeeId, String name, String password) throws XPathExpressionException, SAXException, IOException,
//			ParserConfigurationException, ChannyException {
//		Role r = RoleDao.getRoleByName("客户");
//		if (r == null) {
//			throw new ChannyException(ErrorCode.OBJECT_NOT_EXISTED, String.format("角色不存在: %s", "客户"));
//		}
//
//		if (UserDao.exists(employeeId)) {
//			throw new ChannyException(ErrorCode.USER_EXISTED);
//		}
//
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//
//		Client client = new Client();
//		client.setEmployeeId(employeeId);
//		client.setName(name);
//		password = Sha1.encode(password);
//		client.setPassword(password);
//		client.setDateCreated(new Date());
//
//		client.setRole(r);
//		session.save(client);
//
//		session.getTransaction().commit();
//	}
//	
//	public static Client getByName(String name) {
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		Query query = session.createQuery("from Client client where client.name = :name");
//		query.setParameter("name", name);
//		@SuppressWarnings("unchecked")
//		List<Client> result = query.list();
//		session.getTransaction().commit();
//		if (result.size() == 0) {
//			return null;
//		} else {
//			return result.get(0);
//		}
//	}

	@Override
	public Client getById(long id) {
		return super.getById(id, Client.class);
	}

	@Override
	public boolean exists(long id) {
		return getById(id) != null;
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, Client.class);
	}

	@Override
	public QueryResult<Client> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, Client.class);
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(Client.class, filter);
	}
	
	public Client getByName(String name) {
		List<Client> clients = super.getByField("name", name, Client.class);
		if (clients.isEmpty()) {
			return null;
		}
		
		return clients.get(0);
	}
}
