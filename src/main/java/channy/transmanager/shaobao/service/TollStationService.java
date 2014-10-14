package channy.transmanager.shaobao.service;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import channy.transmanager.shaobao.data.TollStationDao;
import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.model.TollStation;
import channy.util.ChannyException;
import channy.util.ErrorCode;

public class TollStationService implements ServiceInterface<TollStation> {
	private TollStationDao dao = new TollStationDao();
	
	public TollStation add(String name, String description) throws ChannyException {
		if (dao.getByName(name) != null) {
			throw new ChannyException(ErrorCode.OBJECT_EXISTED);
		}
		TollStation station = new TollStation();
		station.setName(name);
		station.setDescription(description);
		
		dao.add(station);
		return station;
	}
	
	public TollStation add(String name, String description, double x, double y) throws ChannyException {
		if (dao.getByName(name) != null) {
			throw new ChannyException(ErrorCode.OBJECT_EXISTED);
		}
		TollStation station = new TollStation();
		station.setName(name);
		station.setDescription(description);
		station.setX(x);
		station.setY(y);
		
		dao.add(station);
		return station;
	}
	
	
	public JSONObject select(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException {
		QueryResult<TollStation> result = dao.query(page, pageSize, filter);
		
		List<TollStation> list = result.getData();
		JSONObject res = new JSONObject();
		res.put("match", result.getMatch());

		JSONArray array = new JSONArray();
		for (TollStation e : list) {
			JSONObject obj = new JSONObject();
			obj.put("id", e.getId());
			obj.put("text", e.getName());
			
			array.put(obj);
		}
		
		res.put("data", array);
		
		return res;
	}

	
	public TollStation getById(long id) {
		return dao.getById(id);
	}

	public TollStation getByName(String name) {
		return dao.getByName(name);
	}

	
	public void update(TollStation entity) {
		dao.update(entity);
	}

	
	public void remove(TollStation entity) {
		dao.remove(entity);
	}

	
	public void removeById(long id) throws ChannyException {
		dao.removeById(id);
	}

	
	public boolean exists(long id) {
		return dao.exists(id);
	}

	
	public int getCount() throws ChannyException {
		return dao.getCount(null);
	}

	
	public JSONObject query(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}
