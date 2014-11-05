package channy.transmanager.shaobao.data;

import java.util.List;
import java.util.Map;

import channy.transmanager.shaobao.model.Place;
import channy.util.ChannyException;

public class PlaceDao extends BaseDao<Place> {
	@Override
	public Place getById(long id) {
		return super.getById(id, Place.class);
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, Place.class);
	}

	@Override
	public QueryResult<Place> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, Place.class);
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(Place.class, filter);
	}
	
	public Place getByName(String name) {
		List<Place> places = super.getByField("name", name, Place.class);
		if (places.isEmpty()) {
			return null;
		}
		
		return places.get(0);
	}
}
