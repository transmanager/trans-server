package channy.transmanager.shaobao.data;

import java.util.List;
import java.util.Map;

import channy.transmanager.shaobao.model.TollStation;
import channy.util.ChannyException;

public class TollStationDao extends BaseDao<TollStation> {

	@Override
	public TollStation getById(long id) {
		return super.getById(id, TollStation.class);
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, TollStation.class);
	}

	@Override
	public QueryResult<TollStation> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, TollStation.class);
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(TollStation.class, filter);
	}
	
	public TollStation getByName(String name) {
		List<TollStation> result = super.getByField("name", name, TollStation.class);
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0); 
	}
}
