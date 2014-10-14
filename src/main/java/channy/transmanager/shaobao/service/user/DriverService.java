package channy.transmanager.shaobao.service.user;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import channy.transmanager.shaobao.data.user.DriverDao;
import channy.transmanager.shaobao.model.order.Order;
import channy.transmanager.shaobao.model.user.Driver;

public class DriverService extends UserService {
	private DriverDao dao = new DriverDao();
	
	public JSONObject sync(Driver driver) throws JSONException {
		List<Order> orders = dao.sync(driver);
		JSONObject data = new JSONObject();
		

		JSONArray array = new JSONArray();
		for (Order order : orders) {
			JSONObject obj = new JSONObject();
			obj.put("id", order.getId());
			obj.put("client", order.getClient().getName());
			obj.put("status", order.getStatus().getDesciption());

			if (order.getdId() == null) {
				obj.put("dId", "无");
			} else {
				obj.put("dId", order.getdId());
			}

			if (order.getcId() == null) {
				obj.put("cId", "无");
			} else {
				obj.put("cId", order.getcId());
			}

			if (order.getoId() == null) {
				obj.put("oId", "无");
			} else {
				obj.put("oId", order.getoId());
			}

			array.put(obj);
		}

		data.put("orders", array);
		
		return data;
	}
}
