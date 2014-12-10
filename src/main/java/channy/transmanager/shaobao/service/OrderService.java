package channy.transmanager.shaobao.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import channy.transmanager.shaobao.model.Product;
import channy.transmanager.shaobao.model.Toll;
import channy.transmanager.shaobao.model.TollStation;
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
import channy.transmanager.shaobao.service.user.ClientService;
import channy.transmanager.shaobao.service.user.DriverService;
import channy.transmanager.shaobao.service.vehicle.MotorcadeService;
import channy.transmanager.shaobao.service.vehicle.TruckService;
import channy.util.ChannyException;
import channy.util.ErrorCode;
import channy.util.StringUtil;

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
		dao.schedule(motorcade, driver, truck, client, type, dId, cIds, image, cargo, cargoSource, cargoDestination, oId, ore, oreSource, scheduler, true);
		int total = getCount();
		int page = (total - 1) / 15;
		JSONObject data = new JSONObject();
		data.put("page", page);

		return data;
	}

	public JSONObject scheduleClientOrder(Motorcade motorcade, Driver driver, Truck truck, Client client, OrderType type, User scheduler)
			throws ChannyException, JSONException {
		dao.schedule(motorcade, driver, truck, client, type, null, null, null, null, null, null, null, null, null, scheduler, false);
		int total = getCount();
		int page = (total - 1) / 15;
		JSONObject data = new JSONObject();
		data.put("page", page);

		return data;
	}

	public JSONObject makeup(Motorcade motorcade, Driver driver, Truck truck, Client client, OrderType type, String dId, List<String> cIds,
			List<Image> image, List<Cargo> cargo, Place cargoSource, Place cargoDestination, Date dateDeparted, Date dateArrived, String oId,
			Ore ore, Place oreSource, Place oreDestination, Date dateReturn, Date dateReturned, double finalCargoWeight, double oreWeight,
			double finalOreWeight, double odometerStart, double odometerEnd, List<Expenses> expenses, List<Fine> fines, List<Toll> tolls,
			double fuelUsed) throws ChannyException, JSONException {
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
				status = OrderStatus.ReadyToDepart;
			} else {
				status = OrderStatus.WaitingCargo;
			}
			break;
		case WaitingCargo:
			status = OrderStatus.LoadingCargo;
			break;
		case LoadingCargo:
			status = OrderStatus.ReadyToDepart;
			break;
		case ReadyToDepart:
			status = OrderStatus.Outbound;
			order.setDateDeparted(new Date());
			break;
		case Outbound:
			if (order.getOrderType() == OrderType.OreOnly) {
				status = OrderStatus.WaitingOre;
			} else {
				status = OrderStatus.ReadyToUnloadCargo;
			}
			order.setDateArrived(new Date());
			break;
		case ReadyToUnloadCargo:
			status = OrderStatus.UnloadingCargo;
			break;
		case UnloadingCargo:
			if (order.getOrderType() == OrderType.CargoOnly) {
				status = OrderStatus.ReadyToReturn;
				order.setDateReturn(new Date());
			} else {
				status = OrderStatus.CargoUnloaded;
			}
			break;
		case CargoUnloaded:
			status = OrderStatus.WaitingOre;
			break;
		case WaitingOre:
			status = OrderStatus.LoadingOre;
			break;
		case LoadingOre:
			status = OrderStatus.ReadyToReturn;
			break;
		case ReadyToReturn:
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
				status = OrderStatus.ReadyToUnloadOre;
			}
			order.setDateReturned(new Date());
			break;
		case ReadyToUnloadOre:
			status = OrderStatus.UnloadingOre;
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

	public void verify(long id, User verifier, JSONObject data) throws ChannyException, JSONException {
		Order order = dao.getById(id);
		if (order == null) {
			throw new ChannyException(ErrorCode.OBJECT_NOT_EXISTED, "运单不存在");
		}

		if (data == null) {
			throw new ChannyException(ErrorCode.INVALID_DATA);
		}

		if (order.getStatus() == OrderStatus.CargoVerificationPending || order.getStatus() == OrderStatus.CargoVerificationFailed) {
			if (!data.has("type")) {
				throw new ChannyException(ErrorCode.INVALID_DATA, "缺少参数：运单类型");
			}
			OrderType type = OrderType.valueOf(data.getString("type"));
			order.setOrderType(type);
			switch (type) {
			case CargoOnly:
				order.setoId(null);
				order.setOreSource(null);
				order.setOreDestination(null);
				order.setOre(null);
				order.setOreWeight(0);
				order.setFinalOreWeight(0);
				break;
			case OreOnly:
				order.setdId(null);
				order.setcId(null);
				order.setCargo(null);
				order.setCargoWeight(0);
				order.setCargoSource(null);
				order.setCargoDestination(null);
				break;
			default:
				break;
			}

			if (!data.has("motorcade")) {
				throw new ChannyException(ErrorCode.INVALID_DATA, "缺少参数：车队");
			}
			MotorcadeService motorcadeService = new MotorcadeService();
			Motorcade motorcade = motorcadeService.getByName(data.getString("motorcade"));
			if (motorcade == null) {
				motorcade = new Motorcade();
				motorcade.setName(data.getString("motorcade"));
				motorcade.setDescription("系统自动添加");
			}

			if (!data.has("driver")) {
				throw new ChannyException(ErrorCode.INVALID_DATA, "缺少参数：驾驶员");
			}
			DriverService driverService = new DriverService();
			Driver driver = (Driver) driverService.getById(data.getLong("driver"));
			if (driver == null) {
				throw new ChannyException(ErrorCode.USER_NOT_EXISTED);
			}
			order.setDriver(driver);

			if (!data.has("truck")) {
				throw new ChannyException(ErrorCode.INVALID_DATA, "缺少参数：车辆");
			}
			TruckService truckService = new TruckService();
			Truck truck = truckService.getByPlate(data.getString("truck"));
			if (truck == null) {
				truck = new Truck();
				truck.setPlate(data.getString("truck"));
				truck.setDescription("系统自动添加");
				truck.setDriver(driver);
			}
			truck.setMotorcade(motorcade);
			order.setTruck(truck);

			if (!data.has("client")) {
				throw new ChannyException(ErrorCode.INVALID_DATA, "缺少参数：客户");
			}
			ClientService clientService = new ClientService();
			Client client = clientService.getByName(data.getString("client"));
			if (client == null) {
				client = new Client(1);
				client.setName(data.getString("client"));
				client.setDescription("系统自动添加");
				client.setLastOrder(new Date());
				client.setOrderCount(client.getOrderCount() + 1);
			}
			order.setClient(client);

			if (type != OrderType.OreOnly) {
				if (!data.has("cargoInfo")) {
					throw new ChannyException(ErrorCode.INVALID_DATA, "缺少参数：货运信息");
				}
				JSONObject cargoInfo = data.getJSONObject("cargoInfo");
				if (cargoInfo.has("dId")) {
					order.setdId(cargoInfo.getString("dId"));
				}

				if (!cargoInfo.has("cargoSource")) {
					throw new ChannyException(ErrorCode.INVALID_DATA, "缺少参数：货物装车点");
				} else {
					PlaceService placeService = new PlaceService();
					String tmp = cargoInfo.getString("cargoSource");
					Place cargoSource = placeService.getByName(tmp);
					if (cargoSource == null) {
						cargoSource = placeService.add(tmp, "系统自动添加");
					}

					order.setCargoSource(cargoSource);
				}

				if (!cargoInfo.has("cargoDestination")) {
					throw new ChannyException(ErrorCode.INVALID_DATA, "缺少参数：货物到站");
				} else {
					PlaceService placeService = new PlaceService();
					String tmp = cargoInfo.getString("cargoDestination");
					Place cargoDestination = placeService.getByName(tmp);
					if (cargoDestination == null) {
						cargoDestination = placeService.add(tmp, "系统自动添加");
					}

					order.setCargoDestination(cargoDestination);
				}

				if (!cargoInfo.has("cargo")) {
					throw new ChannyException(ErrorCode.BAD_ARGUMENT, "缺少参数：货物明细");
				}
				JSONArray cargoes = cargoInfo.getJSONArray("cargo");
				List<Cargo> cargo = new ArrayList<Cargo>();
				List<String> cIds = new ArrayList<String>();
				for (int i = 0; i < cargoes.length(); i++) {
					JSONObject obj = cargoes.getJSONObject(i);
					JSONArray products = obj.getJSONArray("products");

					String cId = obj.getString("cId");
					for (int j = 0; j < products.length(); j++) {
						JSONObject product = products.getJSONObject(j);
						String name = product.getString("name");
						int amount = product.getInt("amount");
						double weight = product.getDouble("weight");

						ProductService productService = new ProductService();
						Product p = productService.getByName(name);
						if (p == null) {
							p = productService.add(name, "系统自动添加");
						}
						Cargo c = new Cargo();

						c.setProduct(p);
						c.setAmount(amount);
						c.setWeight(weight);
						c.setcId(cId);
						cargo.add(c);
					}
					cIds.add(cId);
				}

				order.setcId(StringUtil.join(cIds, ","));
				order.setCargo(cargo);
			}

			if (type != OrderType.CargoOnly) {
				if (!data.has("oreInfo")) {
					throw new ChannyException(ErrorCode.INVALID_DATA, "缺少参数：矿石信息");
				}
				JSONObject oreInfo = data.getJSONObject("oreInfo");
				if (!oreInfo.has("oId")) {
					throw new ChannyException(ErrorCode.INVALID_DATA, "缺少参数：矿单号");
				}
				order.setoId(oreInfo.getString("oId"));

				if (!oreInfo.has("oreSource")) {
					throw new ChannyException(ErrorCode.INVALID_DATA, "缺少参数：矿石装车点");
				} else {
					PlaceService placeService = new PlaceService();
					String tmp = oreInfo.getString("oreSource");
					Place oreSource = placeService.getByName(tmp);
					if (oreSource == null) {
						oreSource = placeService.add(tmp, "系统自动添加");
					}

					order.setOreSource(oreSource);
				}

				if (oreInfo.has("oreDestination")) {
					PlaceService placeService = new PlaceService();
					String tmp = oreInfo.getString("oreDestination");
					Place oreDestination = placeService.getByName(tmp);
					if (oreDestination == null) {
						oreDestination = placeService.add(tmp, "系统自动添加");
					}

					order.setCargoDestination(oreDestination);
				}

				if (!oreInfo.has("ore")) {
					throw new ChannyException(ErrorCode.INVALID_DATA, "缺少参数：矿石名");
				}
				OreService oreService = new OreService();
				Ore ore = oreService.getByName(oreInfo.getString("ore"));
				if (ore == null) {
					ore = new Ore();
					ore.setName(oreInfo.getString("ore"));
					ore.setDescription("系统自动添加");
				}
				order.setOre(ore);

				if (!oreInfo.has("oreWeight")) {
					throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无矿重信息"));
				}
				order.setOreWeight(oreInfo.getDouble("oreWeight"));

				if (!oreInfo.has("oreFinalWeight")) {
					throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无最终矿重信息"));
				}
				order.setFinalOreWeight(oreInfo.getDouble("oreFinalWeight"));
			}

			order.setStatus(OrderStatus.ExpensesVerificationPending);
			order.setCargoVerifiedBy(verifier);
			order.setCargoVerified(true);
		} else if (order.getStatus() == OrderStatus.ExpensesVerificationPending || order.getStatus() == OrderStatus.ExpensesVerificationFailed) {
			if (!data.has("tollInfo")) {
				throw new ChannyException(ErrorCode.INVALID_DATA, "缺少参数：路费");
			}
			TollStationService tollStationService = new TollStationService();
			JSONArray tollInfo = data.getJSONArray("tollInfo");
			List<Toll> tolls = new ArrayList<Toll>();
			for (int i = 0; i < tollInfo.length(); i++) {
				JSONObject obj = tollInfo.getJSONObject(i);
				Toll toll = new Toll();

				String en = obj.getString("entry");
				TollStation entry = tollStationService.getByName(en);
				if (entry == null) {
					entry = tollStationService.add(en, "系统自动添加");
				}
				toll.setEntry(entry);

				String ex = obj.getString("exit");
				TollStation exit = tollStationService.getByName(ex);
				if (exit == null) {
					exit = tollStationService.add(ex, "系统自动添加");
				}
				toll.setExit(exit);

				toll.setAmount(obj.getDouble("amount"));

				if (obj.has("isLoading")) {
					toll.setLoading(true);
				}

				tolls.add(toll);
			}
			order.setTolls(tolls);

			if (data.has("fineInfo")) {
				JSONArray fineInfo = data.getJSONArray("fineInfo");
				List<Fine> fines = new ArrayList<Fine>();
				for (int i = 0; i < fineInfo.length(); i++) {
					JSONObject obj = fineInfo.getJSONObject(i);
					Fine fine = new Fine();
					if (!obj.has("type")) {
						throw new ChannyException(ErrorCode.INVALID_DATA, "无效的罚单：缺少类型");
					}
					fine.setType(obj.getString("type"));

					if (!obj.has("amount")) {
						throw new ChannyException(ErrorCode.INVALID_DATA, "无效的罚单：缺少金额");
					}
					fine.setAmount(obj.getDouble("amount"));

					fines.add(fine);
				}

				order.setFines(fines);
			} else {
				order.setFines(null);
			}

			if (data.has("expensesInfo")) {
				JSONArray expensesInfo = data.getJSONArray("expensesInfo");
				List<Expenses> e = new ArrayList<Expenses>();
				for (int i = 0; i < expensesInfo.length(); i++) {
					JSONObject obj = expensesInfo.getJSONObject(i);
					Expenses expenses = new Expenses();
					if (!obj.has("type")) {
						throw new ChannyException(ErrorCode.INVALID_DATA, "无效的费用：缺少类型");
					}
					expenses.setType(obj.getString("type"));

					if (!obj.has("amount")) {
						throw new ChannyException(ErrorCode.INVALID_DATA, "无效的费用：缺少金额");
					}
					expenses.setAmount(obj.getDouble("amount"));

					e.add(expenses);
				}

				order.setExpenses(e);
			} else {
				order.setExpenses(null);
			}

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
