package channy.transmanager.shaobao.data;

import java.util.Map;

import channy.transmanager.shaobao.model.Toll;
import channy.util.ChannyException;

public class TollDao extends BaseDao<Toll> {

	@Override
	public Toll getById(long id) {
		return super.getById(id, Toll.class);
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, Toll.class);
	}

	@Override
	public QueryResult<Toll> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, Toll.class);
	}
//	public static Toll add(TollStation entry, TollStation exit, double amount, boolean isLoading) {
//		Toll toll = new Toll();
//		toll.setEntry(entry);
//		toll.setExit(exit);
//		toll.setAmount(amount);
//		toll.setLoading(isLoading);
//		
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		session.save(toll);
//
//		session.getTransaction().commit();
//		
//		return toll;
//	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(Toll.class, filter);
	}
	
	
}
