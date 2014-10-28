package channy.transmanager.shaobao.data.order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import channy.transmanager.shaobao.data.BaseDao;
import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.model.Cargo;
import channy.transmanager.shaobao.model.Expenses;
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
import channy.transmanager.shaobao.model.vehicle.Motorcade;
import channy.transmanager.shaobao.model.vehicle.Truck;
import channy.util.ChannyException;
import channy.util.HibernateUtil;
import channy.util.StringUtil;

public class OrderDao extends BaseDao<Order> {
	// public static boolean exits(String cId, String oId) {
	// return (getByCId(cId) != null && getByOId(oId) != null);
	// }
	//
	// public static boolean existsOId(String oId) {
	// return getByOId(oId) != null;
	// }
	//
	// public static boolean existsCId(String cId) {
	// return getByCId(cId) != null;
	// }
	//
	// public static Order getById(long id) {
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query =
	// session.createQuery("from Order order where order.id = :id");
	// query.setParameter("id", id);
	// @SuppressWarnings("unchecked")
	// List<Order> result = query.list();
	// session.getTransaction().commit();
	// if (result.size() == 0) {
	// return null;
	// } else {
	// return result.get(0);
	// }
	// }

	public Order getByCId(String cId) {
		// Session session = HibernateUtil.openSession();
		// try {
		// session.beginTransaction();
		// Query query =
		// session.createQuery("from Order order where order.cId like :cId");
		// query.setParameter("cId", String.format("%%%s%%", cId));
		// @SuppressWarnings("unchecked")
		// List<Order> result = query.list();
		// session.getTransaction().commit();
		// if (result.size() == 0) {
		// return null;
		// } else {
		// return result.get(0);
		// }
		// } finally {
		// session.close();
		// }

		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from Order order where order.cId like :cId");
			query.setParameter("cId", String.format("%%%s%%", cId));
			@SuppressWarnings("unchecked")
			List<Order> result = query.list();
			if (result.size() == 0) {
				return null;
			} else {
				return result.get(0);
			}
		} finally {
			session.getTransaction().commit();
		}
	}

	public Order getByOId(String oId) {
		return (Order) super.getByField("oId", oId, Order.class);
		// Session session =
		// HibernateUtil.getSessionFactory().getCurrentSession();
		// session.beginTransaction();
		// Query query =
		// session.createQuery("from Order order where order.oId = :oId");
		// query.setParameter("oId", oId);
		// @SuppressWarnings("unchecked")
		// List<Order> result = query.list();
		// session.getTransaction().commit();
		// if (result.size() == 0) {
		// return null;
		// } else {
		// return result.get(0);
		// }
	}

	// public static void addRoundTripOrder(Scheduler scheduler, Driver driver,
	// Client client, List<String> cIds, List<Image> Image, List<Cargo> cargo,
	// Place cargoSource, Place cargoDestination, String oId, Ore ore, Place
	// oreSource, Place oreDestination) {
	// Order order = new Order();
	// OrderType type = OrderType.RoundTrip;
	// order.setOrderType(type);
	// order.setClient(client);
	// order.setDriver(driver);
	// order.setTruck(driver.getTruck());
	//
	// String cId = StringUtil.join(cIds, ",");
	// order.setcId(cId);
	// order.setImage(Image);
	//
	// order.setCargo(cargo);
	// order.setCargoSource(cargoSource);
	// order.setCargoDestination(cargoDestination);
	//
	// order.setoId(oId);
	// order.setOre(ore);
	// order.setOreSource(oreSource);
	// order.setOreDestination(oreDestination);
	//
	// order.setDateCreated(new Date());
	//
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// session.save(order);
	//
	// session.getTransaction().commit();
	// }
	//
	// public static void addCargoOnlyOrder(Scheduler scheduler, Driver driver,
	// Client client, List<String> cIds, List<Image> Image, List<Cargo> cargo,
	// Place cargoSource, Place cargoDestination) {
	// Order order = new Order();
	// OrderType type = OrderType.CargoOnly;
	// order.setOrderType(type);
	// order.setClient(client);
	// order.setDriver(driver);
	// order.setTruck(driver.getTruck());
	//
	// String cId = StringUtil.join(cIds, ",");
	// order.setcId(cId);
	// order.setImage(Image);
	//
	// order.setCargo(cargo);
	// order.setCargoSource(cargoSource);
	// order.setCargoDestination(cargoDestination);
	//
	// order.setDateCreated(new Date());
	//
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// session.save(order);
	//
	// session.getTransaction().commit();
	// }
	//
	// public static void addOreOnlyOrder(Scheduler scheduler, Driver driver,
	// Client client, List<String> cIds, List<Image> Image, String oId, Ore ore,
	// Place oreSource, Place oreDestination) {
	// Order order = new Order();
	// OrderType type = OrderType.RoundTrip;
	// order.setOrderType(type);
	// order.setClient(client);
	// order.setDriver(driver);
	// order.setTruck(driver.getTruck());
	//
	// String cId = StringUtil.join(cIds, ",");
	// order.setcId(cId);
	// order.setImage(Image);
	//
	// order.setoId(oId);
	// order.setOre(ore);
	// order.setOreSource(oreSource);
	// order.setOreDestination(oreDestination);
	//
	// order.setDateCreated(new Date());
	//
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// session.save(order);
	//
	// session.getTransaction().commit();
	// }

	// public static void update(Order order) {
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// session.update(order);
	// session.getTransaction().commit();
	// }

	public Order schedule(Motorcade motorcade, Driver driver, Truck truck, Client client, OrderType type, String dId, List<String> cIds,
			List<Image> image, List<Cargo> cargo, Place cargoSource, Place cargoDestination, String oId, Ore ore, Place oreSource, User scheduler) {
		Order order = new Order();
		order.setOrderType(type);
		order.setClient(client);
		order.setDriver(driver);
		order.setTruck(truck);

		order.setStatus(OrderStatus.New);

		order.setdId(dId);

		String cId = null;
		if (cIds != null) {
			cId = StringUtil.join(cIds, ",");
		}
		order.setcId(cId);
		order.setImage(image);

		order.setCargo(cargo);
		order.setCargoSource(cargoSource);
		order.setCargoDestination(cargoDestination);

		order.setoId(oId);
		order.setOre(ore);
		order.setOreSource(oreSource);

		double cargoWeight = 0;
		if (cargo != null) {
			for (Cargo c : cargo) {
				cargoWeight += c.getWeight();
			}
		}

		order.setCargoWeight(cargoWeight);

		order.setScheduler(scheduler);
		// TODO: Update scheduler data

		super.add(order);

		return order;
	}

	public Order makeup(Motorcade motorcade, Driver driver, Truck truck, Client client, OrderType type, String dId, List<String> cIds,
			List<Image> image, List<Cargo> cargo, Place cargoSource, Place cargoDestination, Date dateDeparted, Date dateArrived, String oId,
			Ore ore, Place oreSource, Place oreDestination, Date dateReturn, Date dateReturned, double finalCargoWeight, double oreWeight,
			double finalOreWeight, double odometerStart, double odometerEnd, List<Expenses> expenses, List<Toll> tolls, double fuelUsed) {
		Order order = new Order();
		order.setOrderType(type);
		order.setClient(client);
		order.setDriver(driver);
		order.setTruck(truck);

		order.setStatus(OrderStatus.CargoVerificationPending);

		order.setdId(dId);

		String cId = null;
		if (cIds != null) {
			cId = StringUtil.join(cIds, ",");
		}
		order.setcId(cId);
		order.setImage(image);

		order.setCargo(cargo);
		order.setCargoSource(cargoSource);
		order.setCargoDestination(cargoDestination);

		order.setDateDeparted(dateDeparted);
		order.setDateArrived(dateArrived);
		order.setDateReturn(dateReturn);
		order.setDateReturned(dateReturned);

		order.setoId(oId);
		order.setOre(ore);
		order.setOreSource(oreSource);
		order.setOreDestination(oreDestination);

		double cargoWeight = 0;
		if (cargo != null) {
			for (Cargo c : cargo) {
				cargoWeight += c.getWeight();
			}
		}
		order.setFinalCargoWeight(finalCargoWeight);

		order.setCargoWeight(cargoWeight);
		order.setOreWeight(oreWeight);
		order.setFinalOreWeight(finalOreWeight);

		order.setOdometerStart(odometerStart);
		order.setOdometerEnd(odometerEnd);
		order.setDistance(odometerEnd - odometerStart);

		order.setExpenses(expenses);
		order.setTolls(tolls);

		order.setFuelUsed(fuelUsed);

		super.add(order);
		return order;
	}

	@Override
	public Order getById(long id) {
		return super.getById(id, Order.class);
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, Order.class);
	}

	@Override
	public QueryResult<Order> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			QueryResult<Order> result = super.query(page, pageSize, filter, session, Order.class);
			List<Order> orders = result.getData();
			for (Order order : orders) {
				order.getTruck().getMotorcade().getName();
				order.getDriver().getName();
			}
			return result;
		} finally {
			session.getTransaction().commit();
		}
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(Order.class, filter);
	}

	public Order getDetailById(long id) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			Order order = super.getById(id, session, Order.class);
			order.getTruck().getMotorcade().getName();
			order.getDriver().getName();
			order.getClient().getName();
			order.getImage().size();
			if (order.getCargoSource() != null) {
				order.getCargoSource().getName();
			}

			if (order.getCargoDestination() != null) {
				order.getCargoDestination().getName();
			}

			if (order.getOreSource() != null) {
				order.getOreSource().getName();
			}

			if (order.getOreDestination() != null) {
				order.getOreDestination().getName();
			}

			if (order.getOre() != null) {
				order.getOre().getName();
			}

			if (order.getTolls() != null) {
				order.getTolls().size();
			}

			if (order.getAbnormalities() != null) {
				order.getAbnormalities().size();
			}

			if (order.getExpenses() != null) {
				order.getExpenses().size();
			}

			if (order.getCargo() != null) {
				order.getCargo().size();
			}

			return order;
		} finally {
			session.getTransaction().commit();
		}
	}

	public Order getDetailById(long id, Session session) {
		return super.getById(id, session, Order.class);
	}

	// public static JSONObject query(int page, int pageSize, Map<String,
	// Object> filter) throws JSONException, ChannyException {
	// int total = OrderDao.getCount(null);
	//
	// filter.put(HibernateUtil.ClassKey, Order.class);
	// int match = OrderDao.getCount(filter);
	//
	// String hql = HibernateUtil.assembleHql(filter);
	//
	// System.out.println(hql);
	//
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query = session.createQuery(hql);
	// HibernateUtil.assembleQuery(query, filter);
	// if (page >= 0 && pageSize >= 0) {
	// int offset = page * pageSize;
	// if (offset >= match) {
	// offset = (match - 1) / pageSize * pageSize;
	// }
	// query.setFirstResult(offset);
	// query.setMaxResults(pageSize);
	// }
	//
	// @SuppressWarnings("unchecked")
	// List<Order> list = query.list();
	// session.getTransaction().commit();
	//
	// JSONObject result = new JSONObject();
	// JSONArray data = new JSONArray();
	// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	// for (Order order : list) {
	// JSONObject obj = new JSONObject();
	// String truck = "";
	// String driver = "";
	// String client = "";
	//
	// String dateDeparted = "未出发";
	//
	// String dId = order.getdId();
	// String cId = order.getcId();
	// String oId = order.getoId();
	//
	// if (order.getTruck() != null) {
	// truck = order.getTruck().getPlate();
	// }
	// if (order.getDriver() != null) {
	// driver = order.getDriver().getName();
	// }
	// if (order.getClient() != null) {
	// client = order.getClient().getName();
	// }
	// if (order.getDateDeparted() != null) {
	// dateDeparted = format.format(order.getDateDeparted());
	// }
	//
	// obj.put("id", order.getId());
	// obj.put("motorcade", order.getTruck().getMotorcade().getName());
	// obj.put("truck", truck);
	// obj.put("driver", driver);
	// obj.put("client", client);
	// obj.put("dateDeparted", dateDeparted);
	// obj.put("status", order.getStatus().getDesciption());
	//
	// if (dId == null) {
	// obj.put("dId", "无");
	// } else {
	// obj.put("dId", dId);
	// }
	//
	// if (cId == null) {
	// obj.put("cId", "无");
	// } else {
	// obj.put("cId", cId);
	// }
	//
	// if (oId == null) {
	// obj.put("oId", "无");
	// } else {
	// obj.put("oId", oId);
	// }
	//
	// obj.put("distance", order.getDistance());
	// data.put(obj);
	// }
	//
	// result.put("total", total);
	// result.put("match", match);
	// result.put("data", data);
	//
	// return result;
	// }

	// public static JSONObject syncDriver(Driver driver) throws JSONException {
	// JSONObject data = new JSONObject();
	// String hql = String
	// .format("from Order order where order.driver = :driver and order.status != :s0 and order.status != :s1 and order.status != :s2 and order.status != :s3");
	// Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// Query query = session.createQuery(hql);
	// query.setParameter("driver", driver);
	// query.setParameter("s0", OrderStatus.ExpensesVerificationPending);
	// query.setParameter("s1", OrderStatus.CargoVerificationPending);
	// query.setParameter("s2", OrderStatus.ClearancePending);
	// query.setParameter("s3", OrderStatus.Closed);
	// @SuppressWarnings("unchecked")
	// List<Order> result = query.list();
	// session.getTransaction().commit();
	//
	// JSONArray array = new JSONArray();
	// for (Order order : result) {
	// JSONObject obj = new JSONObject();
	// obj.put("id", order.getId());
	// obj.put("client", order.getClient().getName());
	// obj.put("status", order.getStatus().getDesciption());
	//
	// if (order.getdId() == null) {
	// obj.put("dId", "无");
	// } else {
	// obj.put("dId", order.getdId());
	// }
	//
	// if (order.getcId() == null) {
	// obj.put("cId", "无");
	// } else {
	// obj.put("cId", order.getcId());
	// }
	//
	// if (order.getoId() == null) {
	// obj.put("oId", "无");
	// } else {
	// obj.put("oId", order.getoId());
	// }
	//
	// array.put(obj);
	// }
	//
	// data.put("orders", array);
	// return data;
	// }
	//
	// public static OrderStatus updateStatus(Order order) {
	// Driver driver = order.getDriver();
	// Truck truck = order.getTruck();
	// OrderStatus status = order.getStatus();
	// boolean update = true;
	// switch (status) {
	// case New:
	// if (order.getOrderType() == OrderType.OreOnly) {
	// status = OrderStatus.Outbound;
	// } else {
	// status = OrderStatus.WaitingCargo;
	// }
	// case WaitingCargo:
	// status = OrderStatus.LoadingCargo;
	// break;
	// case LoadingCargo:
	// status = OrderStatus.Outbound;
	// order.setDateDeparted(new Date());
	// break;
	// case Outbound:
	// if (order.getOrderType() == OrderType.OreOnly) {
	// status = OrderStatus.WaitingOre;
	// } else {
	// status = OrderStatus.UnloadingCargo;
	// }
	// order.setDateArrived(new Date());
	// break;
	// case UnloadingCargo:
	// if (order.getOrderType() == OrderType.CargoOnly) {
	// status = OrderStatus.Inbound;
	// order.setDateReturn(new Date());
	// } else {
	// status = OrderStatus.WaitingOre;
	// }
	// break;
	// case WaitingOre:
	// status = OrderStatus.LoadingOre;
	// break;
	// case LoadingOre:
	// status = OrderStatus.Inbound;
	// order.setDateReturn(new Date());
	// break;
	// case Inbound:
	// if (order.getOrderType() == OrderType.CargoOnly) {
	// status = OrderStatus.CargoVerificationPending;
	// driver.setStatus(UserStatus.Idle);
	// UserDao.update(driver);
	// truck.setStatus(TruckStatus.Idle);
	// TruckDao.update(truck);
	// } else {
	// status = OrderStatus.UnloadingOre;
	// }
	// order.setDateReturned(new Date());
	// break;
	// case UnloadingOre:
	// status = OrderStatus.CargoVerificationPending;
	// driver.setStatus(UserStatus.Idle);
	// UserDao.update(driver);
	// truck.setStatus(TruckStatus.Idle);
	// TruckDao.update(truck);
	// break;
	// default:
	// update = false;
	// break;
	// }
	//
	// if (update) {
	// order.setStatus(status);
	// OrderDao.update(order);
	// }
	// return status;
	// }

	// public static void main(String[] args) throws JSONException,
	// ChannyException {
	// // Map<String, Object> filter = new HashMap<String, Object>();
	// // Map<String, Object> truckFilter = new HashMap<String, Object>();
	// // // Truck truck = TruckDao.getByPlate("粤F05941");
	// // truckFilter.put(HibernateUtil.ClassKey, Truck.class);
	// // truckFilter.put("plate", "粤F0593");
	// //
	// // filter.put(HibernateUtil.ClassKey, Order.class);
	// // filter.put("dId", null);
	// // System.out.println(query(0, 15, filter));
	//
	// // Order order = new Order();
	// // System.out.println(order.getId());
	// // Session session =
	// // HibernateUtil.getSessionFactory().getCurrentSession();
	// // String hql =
	// //
	// "from Order order where order.truck in (from Truck truck where truck.plate like :plate)";
	// // session.beginTransaction();
	// // Query query = session.createQuery(hql);
	// // query.setParameter("plate", "%粤F0593%");
	// // @SuppressWarnings("unchecked")
	// // List<Order> list = query.list();
	// // session.getTransaction().commit();
	// //
	// // System.out.println(list.size());
	//
	// Driver driver = DriverDao.getByEmployeeId("10001");
	// System.out.println(OrderDao.syncDriver(driver));
	// }

}
