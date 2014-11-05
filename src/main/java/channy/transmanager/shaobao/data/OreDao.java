package channy.transmanager.shaobao.data;

import java.util.List;
import java.util.Map;

import channy.transmanager.shaobao.model.Ore;
import channy.util.ChannyException;

public class OreDao extends BaseDao<Ore> {
	public Ore getByName(String name) {
		List<Ore> ores =  super.getByField("name", name, Ore.class);
		if (ores.isEmpty()) {
			return null;
		}
		
		return ores.get(0);
	}
	
	@Override
	public Ore getById(long id) {
		return super.getById(id, Ore.class);
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, Ore.class);
	}

	@Override
	public QueryResult<Ore> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, Ore.class);
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(Ore.class, filter);
	}
}
