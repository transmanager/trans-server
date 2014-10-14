package channy.transmanager.shaobao.data.user;

import java.util.Map;

import channy.transmanager.shaobao.data.BaseDao;
import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.model.user.Scheduler;
import channy.util.ChannyException;

public class SchedulerDao extends BaseDao<Scheduler> {

	@Override
	public Scheduler getById(long id) {
		return super.getById(id, Scheduler.class);
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, Scheduler.class);
	}

	@Override
	public QueryResult<Scheduler> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, Scheduler.class);
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(Scheduler.class, filter);
	}

}
