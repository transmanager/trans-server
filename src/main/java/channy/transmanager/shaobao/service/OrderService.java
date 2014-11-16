package channy.transmanager.shaobao.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.data.order.OrderDao;
import channy.transmanager.shaobao.model.Cargo;
import channy.transmanager.shaobao.model.Expenses;
import channy.transmanager.shaobao.model.Fine;
import channy.transmanager.shaobao.model.Image;
import channy.transmanager.shaobao.model.Ore;
import channy.transmanager.shaobao.model.Place;
import channy.transmanager.shaobao.model.Toll;
import channy.transmanager.shaobao.model.order.Order;
import channy.transmanager.shaobao.model.order.OrderStatus;
import channy.transmanager.shaobao.model.order.OrderType;
import channy.transmanager.shaobao.model.user.Client;
import channy.transmanager.shaobao.model.user.Driver;
import channy.transmanager.shaobao.model.user.User;
import channy.transmanager.shaobao.model.user.UserStatus;
import channy.transmanager.shaobao.model.vehicle.Motorcade;
import channy.transmanager.shaobao.model.vehicle.Truck;
import channy.transmanager.shaobao.model.vehicle.TruckStatus;
import channy.transmanager.shaobao.service.user.DriverService;
import channy.transmanager.shaobao.service.vehicle.TruckService;
import channy.util.ChannyException;
import channy.util.ErrorCode;

public class OrderService implements ServiceInterface<Order> {
	private OrderDao dao = new OrderDao();

	public Order getById(long id) {
		return dao.getById(id);
	}

	public Order getByName(String name) {
		return null;
	}

	public void update(Order entity) {
		dao.update(entity);
	}

	public void remove(Order entity) {
		dao.remove(entity);
	}

	public void removeById(long id) throws ChannyException {
		dao.removeById(id);
	}

	public boolean exists(long id) {
		return dao.exists(id);
	}

	public int getCount() throws ChannyException {
		return dao.getCount(null);
	}

	public JSONObject query(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException {
		QueryResult<Order> orders = dao.query(page, pageSize, filter);

		JSONObject result = new JSONObject();
		JSONArray data = new JSONArray();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (Order order : orders.getData()) {
			JSONObject obj = new JSONObject();
			String truck = "";
			String driver = "";
			String client = "";

			String dateDeparted = "未出发";

			String dId = order.getdId();
			String cId = order.getcId();
			String oId = order.getoId();

			if (order.getTruck() != null) {
				truck = order.getTruck().getPlate();
			}
			if (order.getDriver() != null) {
				driver = order.getDriver().getName();
			}
			if (order.getClient() != null) {
				client = order.getClient().getName();
			}
			if (order.getDateDeparted() != null) {
				dateDeparted = format.format(order.getDateDeparted());
			}

			obj.put("id", order.getId());
			obj.put("motorcade", order.getTruck().getMotorcade().getName());
			obj.put("truck", truck);
			obj.put("driver", driver);
			obj.put("client", client);
			obj.put("dateDeparted", dateDeparted);
			obj.put("status", order.getStatus().getDesciption());

			if (dId == null) {
				obj.put("dId", "无");
			} else {
				obj.put("dId", dId);
			}

			if (cId == null) {
				obj.put("cId", "无");
			} else {
				obj.put("cId", cId);
			}

			if (oId == null) {
				obj.put("oId", "无");
			} else {
				obj.put("oId", oId);
			}

			obj.put("distance", order.getDistance());
			data.put(obj);
		}

		result.put("total", orders.getTotal());
		result.put("match", orders.getMatch());
		result.put("data", data);

		return result;
	}

	public JSONObject select(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	public JSONObject scheduleSelfOrder(Motorcade motorcade, Driver driver, Truck truck, Client client, OrderType type, String dId,
			List<String> cIds, List<Image> image, List<Cargo> cargo, Place cargoSource, Place cargoDestination, String oId, Ore ore, Place oreSource,
			User scheduler) throws ChannyException, JSONException {
		dao.schedule(motorcade, driver, truck, client, type, dId, cIds, image, cargo, cargoSource, cargoDestination, oId, ore, oreSource, scheduler);
		int total = getCount();
		int page = (total - 1) / 15;
		JSONObject data = new JSONObject();
		data.put("page", page);

		return data;
	}

	public JSONObject scheduleClientOrder(Motorcade motorcade, Driver driver, Truck truck, Client client, OrderType type, User scheduler)
			throws ChannyException, JSONException {
		dao.schedule(motorcade, driver, truck, client, type, null, null, null, null, null, null, null, null, null, scheduler);
		int total = getCount();
		int page = (total - 1) / 15;
		JSONObject data = new JSONObject();
		data.put("page", page);

		return data;
	}

	public JSONObject makeup(Motorcade motorcade, Driver driver, Truck truck, Client client, OrderType type, String dId, List<String> cIds,
			List<Image> image, List<Cargo> cargo, Place cargoSource, Place cargoDestination, Date dateDeparted, Date dateArrived, String oId,
			Ore ore, Place oreSource, Place oreDestination, Date dateReturn, Date dateReturned, double finalCargoWeight, double oreWeight,
			double finalOreWeight, double odometerStart, double odometerEnd, List<Expenses> expenses, List<Fine> fines, List<Toll> tolls, double fuelUsed)
			throws ChannyException, JSONException {
		dao.makeup(motorcade, driver, truck, client, type, dId, cIds, image, cargo, cargoSource, cargoDestination, dateDeparted, dateArrived, oId,
				ore, oreSource, oreDestination, dateReturn, dateReturned, finalCargoWeight, oreWeight, finalOreWeight, odometerStart, odometerEnd,
				expenses, fines, tolls, fuelUsed);
		int total = getCount();
		int page = (total - 1) / 15;
		JSONObject data = new JSONObject();
		data.put("page", page);

		return data;
	}

	public JSONObject updateStatus(Order order) throws JSONException {
		Driver driver = order.getDriver();
		Truck truck = order.getTruck();
		OrderStatus status = order.getStatus();
		boolean update = true;
		switch (status) {
		case New:
			if (order.getOrderType() == OrderType.OreOnly) {
				status = OrderStatus.Outbound;
			} else {
				status = OrderStatus.WaitingCargo;
			}
		case WaitingCargo:
			status = OrderStatus.LoadingCargo;
			break;
		case LoadingCargo:
			status = OrderStatus.Outbound;
			order.setDateDeparted(new Date());
			break;
		case Outbound:
			if (order.getOrderType() == OrderType.OreOnly) {
				status = OrderStatus.WaitingOre;
			} else {
				status = OrderStatus.UnloadingCargo;
			}
			order.setDateArrived(new Date());
			break;
		case UnloadingCargo:
			if (order.getOrderType() == OrderType.CargoOnly) {
				status = OrderStatus.Inbound;
				order.setDateReturn(new Date());
			} else {
				status = OrderStatus.WaitingOre;
			}
			break;
		case WaitingOre:
			status = OrderStatus.LoadingOre;
			break;
		case LoadingOre:
			status = OrderStatus.Inbound;
			order.setDateReturn(new Date());
			break;
		case Inbound:
			if (order.getOrderType() == OrderType.CargoOnly) {
				status = OrderStatus.CargoVerificationPending;
				driver.setStatus(UserStatus.Idle);
				// UserDao.update(driver);
				truck.setStatus(TruckStatus.Idle);
				// TruckDao.update(truck);
			} else {
				status = OrderStatus.UnloadingOre;
			}
			order.setDateReturned(new Date());
			break;
		case UnloadingOre:
			status = OrderStatus.CargoVerificationPending;
			driver.setStatus(UserStatus.Idle);
			// UserDao.update(driver);
			truck.setStatus(TruckStatus.Idle);
			// TruckDao.update(truck);
			break;
		case CargoVerificationFailed:
			status = OrderStatus.CargoVerificationPending;
			break;
		case ExpensesVerificationFailed:
			status = OrderStatus.ExpensesVerificationPending;
			break;
		case NotClear:
			status = OrderStatus.ClearancePending;
			break;
		default:
			update = false;
			break;
		}

		if (update) {
			order.setStatus(status);
			dao.update(order);
		}

		JSONObject data = new JSONObject();
		data.put("status", status.getDesciption());
		return data;
	}

	public JSONObject scheduleTruckDriver() throws JSONException, ChannyException {
		TruckService truckService = new TruckService();
		Truck truck = truckService.getNextCandidate();

		if (truck == null) {
			throw new ChannyException(ErrorCode.GENERIC_ERROR, "暂无空闲车辆");
		}

		DriverService driverService = new DriverService();
		Driver driver = driverService.getNextCandidate();

		if (driver == null) {
			throw new ChannyException(ErrorCode.GENERIC_ERROR, "暂无空闲驾驶员");
		}

		JSONObject data = new JSONObject();
		JSONObject motorcade = new JSONObject();
		motorcade.put("id", truck.getMotorcade().getId());
		motorcade.put("text", truck.getMotorcade().getName());
		data.put("motorcade", motorcade);

		JSONObject t = new JSONObject();
		t.put("id", truck.getId());
		t.put("text", truck.getPlate());
		data.put("truck", t);

		JSONObject d = new JSONObject();
		d.put("id", driver.getId());
		d.put("text", driver.getName());
		data.put("driver", d);

		return data;
	}

	public Order getDetailById(long id) {
		return dao.getDetailById(id);
	}

	public Order getDetailById(long id, Session session) {
		return dao.getDetailById(id, session);
	}

	public void removeExpired(List<Image> images) {
		// if (images == null) {
		// return;
		// }
		//
		// List<Image> toRemove = new ArrayList<Image>();
		// for (Image image : images) {
		// if (image.getLastModified() == null) {
		// //toRemove.add(image);
		// continue;
		// }
		//
		// long delta = new Date().getTime() -
		// image.getLastModified().getTime();
		// System.out.println(delta);
		// if (delta > 1800000) {
		// toRemove.add(image);
		// }
		// }
		//
		// for (Image image : toRemove) {
		// images.remove(image);
		// }
	}

	public void verify(long id, User verifier) throws ChannyException {
		Order order = dao.getById(id);
		if (order == null) {
			throw new ChannyException(ErrorCode.OBJECT_NOT_EXISTED, "运单不存在");
		}

		if (order.getStatus() == OrderStatus.CargoVerificationPending || order.getStatus() == OrderStatus.CargoVerificationFailed) {
			order.setStatus(OrderStatus.ExpensesVerificationPending);
			order.setCargoVerifiedBy(verifier);
			order.setCargoVerified(true);
		} else if (order.getStatus() == OrderStatus.ExpensesVerificationPending || order.getStatus() == OrderStatus.ExpensesVerificationFailed) {
			order.setStatus(OrderStatus.ClearancePending);
			order.setExpensesVerifiedBy(verifier);
			order.setExpensesVerified(true);
		} else if (order.getStatus() == OrderStatus.ClearancePending || order.getStatus() == OrderStatus.NotClear) {
			order.setStatus(OrderStatus.Closed);
			order.setVerifiedBy(verifier);
		} else {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, "当前运单状态不允许此操作");
		}
		dao.update(order);
	}

	public void reject(long id, String reason, User verifier) throws ChannyException {
		Order order = dao.getById(id);
		if (order == null) {
			throw new ChannyException(ErrorCode.OBJECT_NOT_EXISTED, "运单不存在");
		}

		if (order.getStatus() == OrderStatus.CargoVerificationPending) {
			order.setStatus(OrderStatus.CargoVerificationFailed);
			order.setCargoVerifiedBy(verifier);
			order.setCargoVerified(false);
		} else if (order.getStatus() == OrderStatus.ExpensesVerificationPending) {
			order.setStatus(OrderStatus.ExpensesVerificationFailed);
			order.setExpensesVerified(false);
			order.setExpensesVerifiedBy(verifier);
		} else if (order.getStatus() == OrderStatus.ClearancePending) {
			order.setStatus(OrderStatus.NotClear);
			order.setVerifiedBy(verifier);
		} else {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, "当前运单状态不允许此操作");
		}

		order.setDescription(reason);
		dao.update(order);
	}
}
