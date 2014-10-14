package channy.transmanager.shaobao.service;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import channy.transmanager.shaobao.data.OreDao;
import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.model.Ore;
import channy.util.ChannyException;
import channy.util.ErrorCode;

public class OreService implements ServiceInterface<Ore> {
	private OreDao dao = new OreDao();
	
	public Ore add(String name, String description) throws ChannyException {
		if (dao.getByName(name) != null) {
			throw new ChannyException(ErrorCode.OBJECT_EXISTED);
		}
		
		Ore ore = new Ore();
		ore.setName(name);
		ore.setDescription(description);
		dao.add(ore);
		
		return ore;
	}
	
	
	public Ore getById(long id) {
		return dao.getById(id);
	}

	public Ore getByName(String name) {
		return dao.getByName(name);
	}

	
	public void update(Ore entity) {
		dao.update(entity);
	}

	
	public void remove(Ore entity) {
		dao.remove(entity);
	}

	
	public void removeById(long id) throws ChannyException {
		dao.removeById(id);
	}

	
	public boolean exists(long id) {
		return dao.exists(id);
	}

	
	public JSONObject query(int page, int pageSize, Map<String, Object> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public JSONObject select(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException {
		QueryResult<Ore> result = dao.query(page, pageSize, filter);
		
		List<Ore> list = result.getData();
		JSONObject res = new JSONObject();
		res.put("match", result.getMatch());

		JSONArray array = new JSONArray();
		for (Ore e : list) {
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
