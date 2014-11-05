package channy.transmanager.shaobao.data;

import java.util.List;
import java.util.Map;

import channy.transmanager.shaobao.model.Product;
import channy.util.ChannyException;

public class ProductDao extends BaseDao<Product> {
	public Product getByName(String name) {
		List<Product> result = super.getByField("name", name, Product.class);
		if (result.isEmpty()) {
			return null;
		}
		
		return result.get(0);
	}

	@Override
	public Product getById(long id) {
		return super.getById(id, Product.class);
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, Product.class);
	}

	@Override
	public QueryResult<Product> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, Product.class);
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(Product.class, filter);
	}
}
