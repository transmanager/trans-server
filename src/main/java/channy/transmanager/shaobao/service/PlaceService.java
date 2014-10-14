package channy.transmanager.shaobao.service;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import channy.transmanager.shaobao.data.PlaceDao;
import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.model.Place;
import channy.util.ChannyException;
import channy.util.ErrorCode;

public class PlaceService implements ServiceInterface<Place> {
	private PlaceDao dao = new PlaceDao();
	
	public Place add(String name, String description) throws ChannyException {
		if (dao.getByName(name) != null) {
			throw new ChannyException(ErrorCode.OBJECT_EXISTED);
		}
		
		Place place = new Place();
		place.setName(name);
		place.setDescription(description);
		dao.add(place);
		return place;
	}
	
	
	public Place getById(long id) {
		return dao.getById(id);
	}

	public Place getByName(String name) {
		return dao.getByName(name);
	}

	
	public void update(Place entity) {
		dao.update(entity);
	}

	
	public void remove(Place entity) {
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
		QueryResult<Place> result = dao.query(page, pageSize, filter);
		
		List<Place> list = result.getData();
		JSONObject res = new JSONObject();
		res.put("match", result.getMatch());

		JSONArray array = new JSONArray();
		for (Place e : list) {
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
