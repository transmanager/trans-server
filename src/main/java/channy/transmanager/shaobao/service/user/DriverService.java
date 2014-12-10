package channy.transmanager.shaobao.service.user;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.data.user.DriverDao;
import channy.transmanager.shaobao.model.order.Order;
import channy.transmanager.shaobao.model.user.Driver;
import channy.util.ChannyException;

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
			obj.put("truck", order.getTruck().getPlate());
			obj.put("status", order.getStatus().getDesciption());
			obj.put("isSelfOrder", order.isSelfOrder());

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

	public Driver getNextCandidate() {
		return dao.scheduleDriver();
	}

	public JSONObject select(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException {
		QueryResult<Driver> result = dao.query(page, pageSize, filter);
		List<Driver> list = result.getData();

		JSONObject obj = new JSONObject();
		obj.put("total", result.getMatch());

		JSONArray array = new JSONArray();
		for (Driver user : list) {
			JSONObject u = new JSONObject();
			u.put("id", user.getId());
			// u.put("employeeId", user.getEmployeeId());
			u.put("text", user.getName());
			array.put(u);
		}
		obj.put("data", array);

		return obj;
	}

	public JSONObject getAvailableDrivers(String name, int page, int pageSize) throws ChannyException, JSONException {
		QueryResult<Driver> result = dao.getAvailableDrivers(name, page, pageSize);
		List<Driver> list = result.getData();

		JSONObject obj = new JSONObject();
		obj.put("total", result.getMatch());

		JSONArray array = new JSONArray();
		for (Driver user : list) {
			JSONObject u = new JSONObject();
			u.put("id", user.getId());
			// u.put("employeeId", user.getEmployeeId());
			u.put("text", user.getName());
			array.put(u);
		}
		obj.put("data", array);

		return obj;
	}
}
