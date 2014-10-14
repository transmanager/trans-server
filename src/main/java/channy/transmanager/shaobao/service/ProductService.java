package channy.transmanager.shaobao.service;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import channy.transmanager.shaobao.data.ProductDao;
import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.model.Product;
import channy.util.ChannyException;
import channy.util.ErrorCode;

public class ProductService implements ServiceInterface<Product> {
	ProductDao dao = new ProductDao();
	
	public Product add(String name, String description) throws ChannyException {
		if (dao.getByName(name) != null) {
			throw new ChannyException(ErrorCode.OBJECT_EXISTED);
		}
		
		Product product = new Product();
		product.setName(name);
		product.setDescription(description);
		dao.add(product);
		
		return product;
	}
	
	
	public Product getById(long id) {
		return dao.getById(id);
	}

	public Product getByName(String name) {
		return dao.getByName(name);
	}

	
	public void update(Product entity) {
		dao.update(entity);
	}

	
	public void remove(Product entity) {
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
		ProductDao dao = new ProductDao();
		QueryResult<Product> result = dao.query(page, pageSize, filter);
		
		List<Product> list = result.getData();
		JSONObject res = new JSONObject();
		res.put("match", result.getMatch());

		JSONArray array = new JSONArray();
		for (Product e : list) {
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
