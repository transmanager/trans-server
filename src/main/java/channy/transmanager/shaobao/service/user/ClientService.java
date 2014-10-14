package channy.transmanager.shaobao.service.user;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.data.user.ClientDao;
import channy.transmanager.shaobao.model.user.Client;
import channy.transmanager.shaobao.service.ServiceInterface;
import channy.util.ChannyException;
import channy.util.ErrorCode;

public class ClientService implements ServiceInterface<Client> {
	private ClientDao dao = new ClientDao();
	
//	public static Client add(String name, String description) throws ChannyException {
//		ClientDao dao = new ClientDao();
//		if (dao.getByName(name) != null) {
//			throw new ChannyException(ErrorCode.OBJECT_EXISTED);
//		}
//		Client client = new Client();
//		client.setName(name);
//		client.setDescription(description);
//		dao.add(client);
//		return client;
//	}
	
	public Client add(String name, String description) throws ChannyException {
		ClientDao dao = new ClientDao();
		if (dao.getByName(name) != null) {
			throw new ChannyException(ErrorCode.OBJECT_EXISTED);
		}
		Client client = new Client();
		client.setName(name);
		client.setDescription(description);
		dao.add(client);
		return client;
	}

	public Client getById(long id) {
		return dao.getById(id);
	}

	public Client getByName(String name) {
		return dao.getByName(name);
	}

	public void update(Client entity) {
		dao.update(entity);
	}

	public void remove(Client entity) {
		dao.remove(entity);
	}

	public void removeById(long id) throws ChannyException {
		dao.removeById(id);
	}

	public boolean exists(long id) {
		return dao.exists(id);
	}

	public boolean exits(String name) {
		return dao.getByName(name) != null;
	}

	public JSONObject query(int page, int pageSize, Map<String, Object> filter) {
		return null;
	}

	public JSONObject select(int page, int pageSize, Map<String, Object> filter) throws JSONException, ChannyException {
		QueryResult<Client> result = dao.query(page, pageSize, filter);
		
		List<Client> list = result.getData();
		JSONObject res = new JSONObject();
		res.put("match", result.getMatch());

		JSONArray array = new JSONArray();
		for (Client e : list) {
			JSONObject obj = new JSONObject();
			obj.put("id", e.getId());
			obj.put("text", e.getName());
			
			array.put(obj);
		}
		
		res.put("data", array);
		
		return res;
	}

	public int getCount() throws ChannyException {
		return dao.getCount(null);
	}
}
