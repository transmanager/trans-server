package channy.transmanager.shaobao.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import channy.transmanager.shaobao.feature.Action;
import channy.transmanager.shaobao.model.Cargo;
import channy.transmanager.shaobao.model.Expenses;
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
import channy.transmanager.shaobao.service.OrderService;
import channy.transmanager.shaobao.service.OreService;
import channy.transmanager.shaobao.service.PlaceService;
import channy.transmanager.shaobao.service.ProductService;
import channy.transmanager.shaobao.service.TollStationService;
import channy.transmanager.shaobao.service.user.ClientService;
import channy.transmanager.shaobao.service.user.DriverService;
import channy.transmanager.shaobao.service.user.RoleService;
import channy.transmanager.shaobao.service.vehicle.MotorcadeService;
import channy.transmanager.shaobao.service.vehicle.TruckService;
import channy.util.ChannyException;
import channy.util.ErrorCode;
import channy.util.HibernateUtil;
import channy.util.JsonResponse;

@Controller
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	private DriverService driverService = new DriverService();
	private MotorcadeService motorcadeService = new MotorcadeService();
	private TruckService truckService = new TruckService();
	private ClientService clientService = new ClientService();
	private PlaceService placeService = new PlaceService();
	private ProductService productService = new ProductService();
	private OreService oreService = new OreService();
	private OrderService orderService = new OrderService();
	private TollStationService tollStationService = new TollStationService();
	private RoleService roleService = new RoleService();

	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public String order() {
		return "order/order";
	}

	@RequestMapping(value = "/order/client", method = RequestMethod.GET)
	public String client() {
		return "order/client";
	}

	@RequestMapping(value = "/order/self", method = RequestMethod.GET)
	public String self() {
		return "order/self";
	}

	@RequestMapping(value = "/order/makeup", method = RequestMethod.GET)
	public String makeup() {
		return "order/makeup";
	}

	@RequestMapping(value = "/order/self", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String scheduleSelfOrder(@RequestParam("action") String action, HttpServletRequest request) throws JSONException,
			ChannyException, XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		if (Action.valueOf(Action.class, action) != Action.OrderSchedule) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		/*
		 * Basic order information
		 */
		String tmp = request.getParameter("motorcade");
		Motorcade motorcade = null;
		if (tmp != null) {
			motorcade = motorcadeService.getByName(tmp);
			if (motorcade == null) {
				motorcade = new Motorcade();
				motorcade.setName(tmp);
				motorcade.setDescription("系统自动添加");
			}
		}

		tmp = request.getParameter("driver");
		Driver driver = null;
		if (tmp != null) {
			driver = (Driver) driverService.getById(Long.parseLong(tmp));
			if (driver == null) {
				throw new ChannyException(ErrorCode.USER_NOT_EXISTED);
			}
			if (driver.getStatus() != UserStatus.Idle && driver.getStatus() != UserStatus.Normal) {
				throw new ChannyException(ErrorCode.UNSYNC, String.format("用户%s(%s)状态已改变：%s，无法被调度，请刷新后重试", driver.getName(), driver.getEmployeeId(),
						driver.getStatus().getDescription()));
			}

			driver.setStatus(UserStatus.Delivering);
		}

		tmp = request.getParameter("truck");
		Truck truck = null;
		if (tmp != null) {
			truck = truckService.getByPlate(tmp);
			if (truck == null) {
				// truckService.add(tmp, motorcade, "系统自动添加");
				// truck = truckService.getByPlate(tmp);
				truck = new Truck();
				truck.setPlate(tmp);
				truck.setDescription("系统自动添加");
				truck.setDriver(driver);
				truck.setMotorcade(motorcade);
			}

			if (truck.getStatus() != TruckStatus.Idle) {
				throw new ChannyException(ErrorCode.UNSYNC, String.format("车辆%s状态已改变：%s，无法被调度，请刷新后重试", truck.getPlate(), truck.getStatus()
						.getDescription()));
			}
			truck.setStatus(TruckStatus.Scheduled);
			// truck.getDriver().setStatus(UserStatus.Delivering);
			// truckService.update(truck);
		}

		tmp = request.getParameter("client");
		Client client = null;
		if (tmp != null) {
			client = clientService.getByName(tmp);
			if (client == null) {
				client = new Client();
				client.setName(tmp);
				client.setDescription("系统自动添加");
				client.setOrderCount(client.getOrderCount() + 1);
				// clientService.update(client);
			}
		}

		tmp = request.getParameter("orderType");
		OrderType type = null;
		if (tmp != null) {
			type = OrderType.valueOf(tmp);
			if (type == null) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无效的订单类型：%s", tmp));
			}
		} else {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, "无运单类型");
		}
		// ////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * Outbound cargo information
		 */
		String dId = null;
		List<String> cIds = null;
		List<Image> cImage = null;
		List<Cargo> cargo = null;
		Place cargoSource = null;
		Place cargoDestination = null;

		if (type != OrderType.OreOnly) {
			tmp = request.getParameter("cargoInfo");
			if (tmp == null) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无货物信息"));
			}
			JSONObject cargoInfo = new JSONObject(tmp);
			if (cargoInfo.has("dId")) {
				dId = cargoInfo.getString("dId");
			}

			if (!cargoInfo.has("cargoSource")) {
				// throw new ChannyException(ErrorCode.BAD_ARGUMENT,
				// String.format("无货物信息"));
			} else {
				tmp = cargoInfo.getString("cargoSource");
				cargoSource = placeService.getByName(tmp);
				if (cargoSource == null) {
					// cargoSource = placeService.add(tmp, "系统自动添加");
					cargoSource = new Place();
					cargoSource.setName(tmp);
					cargoSource.setDescription("系统自动添加");
				}
			}

			if (!cargoInfo.has("cargoDestination")) {
				// throw new ChannyException(ErrorCode.BAD_ARGUMENT,
				// String.format("无货物信息"));
			} else {
				tmp = cargoInfo.getString("cargoDestination");
				if (cargoSource != null && cargoSource.getName().equals(tmp)) {
					cargoDestination = cargoSource;
				} else {
					cargoDestination = placeService.getByName(tmp);
				}
				if (cargoDestination == null) {
					// cargoDestination = placeService.add(tmp, "系统自动添加");
					cargoDestination = new Place();
					cargoDestination.setName(tmp);
					cargoDestination.setDescription("系统自动添加");
				}
			}

			if (!cargoInfo.has("cargo")) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无货物明细"));
			}
			JSONArray cargoes = cargoInfo.getJSONArray("cargo");
			cargo = new ArrayList<Cargo>();
			cIds = new ArrayList<String>();
			for (int i = 0; i < cargoes.length(); i++) {
				JSONObject obj = cargoes.getJSONObject(i);
				JSONArray products = obj.getJSONArray("products");

				String cId = obj.getString("cId");
				for (int j = 0; j < products.length(); j++) {
					JSONObject product = products.getJSONObject(j);
					String name = product.getString("name");
					int amount = product.getInt("amount");
					double weight = product.getDouble("weight");

					Product p = productService.getByName(name);
					if (p == null) {
						// p = productService.add(name, "系统自动添加");
						p = new Product();
						p.setName(name);
						p.setDescription("系统自动添加");
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
		}
		// //////////////////////////////////////////////////////////////////////////////////////////////////

		/*
		 * Inbound ore information
		 */
		String oId = null;
		Ore ore = null;
		Place oreSource = null;
		Place oreDestination = null;
		if (type != OrderType.CargoOnly) {
			tmp = request.getParameter("oreInfo");
			if (tmp == null) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无矿物信息"));
			}

			JSONObject oreInfo = new JSONObject(tmp);
			if (!oreInfo.has("oId")) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无矿单号"));
			}
			oId = oreInfo.getString("oId");

			if (!oreInfo.has("oreSource")) {
				// throw new ChannyException(ErrorCode.BAD_ARGUMENT,
				// String.format("无装矿点"));
			} else {
				tmp = oreInfo.getString("oreSource");
				if (cargoSource != null && cargoSource.getName().equals(tmp)) {
					oreSource = cargoSource;
				} else if (cargoDestination != null && cargoDestination.getName().equals(tmp)) {
					oreSource = cargoDestination;
				} else {
					oreSource = placeService.getByName(tmp);
				}
				if (oreSource == null) {
					// oreSource = placeService.add(tmp, "系统自动添加");
					oreSource = new Place();
					oreSource.setName(tmp);
					oreSource.setDescription("系统自动添加");
				}
			}

			if (!oreInfo.has("oreDestination")) {
				// throw new ChannyException(ErrorCode.BAD_ARGUMENT,
				// String.format("无装矿点"));
			} else {
				tmp = oreInfo.getString("oreDestination");
				if (cargoSource != null && cargoSource.getName().equals(tmp)) {
					oreDestination = cargoSource;
				} else if (cargoDestination != null && cargoDestination.getName().equals(tmp)) {
					oreDestination = cargoDestination;
				} else if (oreSource != null && oreSource.getName().equals(tmp)) {
					oreDestination = oreSource;
				} else {
					oreDestination = placeService.getByName(tmp);
				}
				if (oreDestination == null) {
					// oreDestination = placeService.add(tmp, "系统自动添加");
					oreDestination = new Place();
					oreDestination.setName(tmp);
					oreDestination.setDescription("系统自动添加");
				}
			}

			if (!oreInfo.has("ore")) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无矿物名称"));
			}
			tmp = oreInfo.getString("ore");
			ore = oreService.getByName(tmp);
			if (ore == null) {
				// ore = oreService.add(tmp, "系统自动添加");
				ore = new Ore();
				ore.setName(tmp);
				ore.setDescription("系统自动添加");
			}
		}

		User scheduler = (User) request.getSession().getAttribute("currentUser");

		// OrderDao.schedule(motorcade, driver, truck, client, type, dId, cIds,
		// cImage, cargo, cargoSource, cargoDestination, oId, ore, oreSource,
		// scheduler);
		// int total = orderService.getCount();
		// int page = (total - 1) / 15;
		// JSONObject data = new JSONObject();
		// data.put("page", page);

		JSONObject data = orderService.scheduleSelfOrder(motorcade, driver, truck, client, type, dId, cIds, cImage, cargo, cargoSource,
				cargoDestination, oId, ore, oreSource, scheduler);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/order/client", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String scheduleClient(@RequestParam("action") String action, HttpServletRequest request) throws JSONException,
			ChannyException, XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		if (Action.valueOf(Action.class, action) != Action.OrderSchedule) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		/*
		 * Basic order information
		 */
		String tmp = request.getParameter("motorcade");
		Motorcade motorcade = null;
		if (tmp != null) {
			motorcade = motorcadeService.getByName(tmp);
			if (motorcade == null) {
				// motorcade = motorcadeService.add(tmp, "系统自动添加");
				motorcade = new Motorcade();
				motorcade.setName(tmp);
				motorcade.setDescription("系统自动添加");
			}
		}

		tmp = request.getParameter("driver");
		System.out.println("driver=" + tmp);
		Driver driver = null;
		if (tmp != null) {
			driver = (Driver) driverService.getById(Long.parseLong(tmp));
			if (driver == null) {
				throw new ChannyException(ErrorCode.USER_NOT_EXISTED);
			}
			if (driver.getStatus() != UserStatus.Idle && driver.getStatus() != UserStatus.Normal) {
				throw new ChannyException(ErrorCode.UNSYNC, String.format("用户%s(%s)状态已改变：%s，无法被调度，请刷新后重试", driver.getName(), driver.getEmployeeId(),
						driver.getStatus().getDescription()));
			}

			driver.setStatus(UserStatus.Delivering);
		}

		tmp = request.getParameter("truck");
		Truck truck = null;
		if (tmp != null) {
			truck = truckService.getByPlate(tmp);
			if (truck == null) {
				truckService.add(motorcade.getName(), tmp);
				truck = truckService.getByPlate(tmp);
				truck.setDriver(driver);
				truck.setMotorcade(motorcade);
			}
			if (truck.getStatus() != TruckStatus.Idle) {
				throw new ChannyException(ErrorCode.UNSYNC, String.format("车辆%s状态已改变：%s，无法被调度，请刷新后重试", truck.getPlate(), truck.getStatus()
						.getDescription()));
			}
			truck.setStatus(TruckStatus.Scheduled);
			// truck.getDriver().setStatus(UserStatus.Delivering);
			// truckService.update(truck);
		}

		tmp = request.getParameter("client");
		Client client = null;
		if (tmp != null) {
			client = clientService.getByName(tmp);
			if (client == null) {
				client = new Client(1);
				// client = clientService.add(tmp, "系统自动添加");
				client.setName(tmp);
				client.setDescription("系统自动添加");
				client.setLastOrder(new Date());
				client.setOrderCount(client.getOrderCount() + 1);
				// clientService.update(client);
			}
		}

		tmp = request.getParameter("orderType");
		OrderType type = null;
		if (tmp != null) {
			type = OrderType.valueOf(tmp);
			if (type == null) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无效的订单类型：%s", tmp));
			}
		} else {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, "无运单类型");
		}

		User scheduler = (User) request.getSession().getAttribute("currentUser");
		orderService.scheduleClientOrder(motorcade, driver, truck, client, type, scheduler);
		return new JsonResponse(ErrorCode.OK).generate();
	}

	@RequestMapping(value = "/order/refreshSchedule", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String refreshTruckDriver(@RequestParam("action") String action, HttpServletRequest request) throws JSONException,
			ChannyException, XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		if (Action.valueOf(Action.class, action) != Action.OrderRefreshSchedule) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		// Truck truck = truckService.getNextCandidate();
		// JSONObject data = new JSONObject();
		//
		// if (truck == null) {
		// return new JsonResponse(ErrorCode.GENERIC_ERROR,
		// "暂无空闲车辆").generate();
		// }
		//
		// JSONObject motorcade = new JSONObject();
		// motorcade.put("id", truck.getMotorcade().getId());
		// motorcade.put("text", truck.getMotorcade().getName());
		// data.put("motorcade", motorcade);
		//
		// JSONObject t = new JSONObject();
		// t.put("id", truck.getId());
		// t.put("text", truck.getPlate());
		// data.put("truck", t);
		//
		// JSONObject driver = new JSONObject();
		// driver.put("id", truck.getDriver().getId());
		// driver.put("text", truck.getDriver().getName());
		// data.put("driver", driver);

		JSONObject data = orderService.scheduleTruckDriver();
		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/order/makeup", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String makeup(@RequestParam("action") String action, HttpServletRequest request) throws JSONException, ChannyException,
			XPathExpressionException, SAXException, IOException, ParserConfigurationException, ParseException {
		if (Action.valueOf(Action.class, action) != Action.OrderMakeup) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		/*
		 * Basic order information
		 */
		String tmp = request.getParameter("motorcade");
		Motorcade motorcade = null;
		if (tmp != null) {
			motorcade = motorcadeService.getByName(tmp);
			if (motorcade == null) {
				// motorcade = motorcadeService.add(tmp, "系统自动添加");
				motorcade = new Motorcade();
				motorcade.setName(tmp);
				motorcade.setDescription("系统自动添加");
			}
		}

		tmp = request.getParameter("driver");
		Driver driver = null;
		if (tmp != null) {
			driver = (Driver) driverService.getById(Long.parseLong(tmp));
			if (driver == null) {
				throw new ChannyException(ErrorCode.USER_NOT_EXISTED);
			}
		}

		tmp = request.getParameter("truck");
		Truck truck = null;
		if (tmp != null) {
			truck = truckService.getByPlate(tmp);
			if (truck == null) {
				// truck = truckService.add(motorcade.getName(), tmp);
				truck = new Truck();
				truck.setPlate(tmp);
				truck.setDescription("系统自动添加");
				truck.setDriver(driver);
				// truckService.update(truck);
			}
		}

		tmp = request.getParameter("client");
		Client client = null;
		if (tmp != null) {
			client = clientService.getByName(tmp);
			if (client == null) {
				// client = clientService.add(tmp, "系统自动添加");
				client = new Client();
				client.setName(tmp);
				client.setDescription("系统自动添加");
				client.setLastOrder(new Date());
				client.setOrderCount(client.getOrderCount() + 1);
				// clientService.update(client);
			}
		}

		tmp = request.getParameter("orderType");
		OrderType type = null;
		if (tmp != null) {
			type = OrderType.valueOf(tmp);
			if (type == null) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无效的订单类型：%s", tmp));
			}
		} else {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, "无运单类型");
		}

		Date dateDeparted = null;
		Date dateArrived = null;
		Date dateReturn = null;
		Date dateReturned = null;
		SimpleDateFormat format = new SimpleDateFormat("M/d/HH:mm");
		SimpleDateFormat format2 = new SimpleDateFormat("yy/M/d/HH:mm");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyy/M/d/HH:mm");
		tmp = request.getParameter("dateDeparted");
		if (tmp == null) {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无出发时间"));
		}
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		tmp.replace("：", ":");
		try {
			dateDeparted = format.parse(tmp);
			calendar.setTime(dateDeparted);
			calendar.set(Calendar.YEAR, year);
			dateDeparted = calendar.getTime();
		} catch (ParseException e) {
			try {
				dateDeparted = format2.parse(tmp);
			} catch (ParseException e2) {
				dateDeparted = format3.parse(tmp);
			}
		}

		tmp = request.getParameter("dateArrived");
		if (tmp == null) {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无到站时间"));
		}
		tmp.replace("：", ":");
		try {
			dateArrived = format.parse(tmp);
			calendar.setTime(dateArrived);
			calendar.set(Calendar.YEAR, year);
			dateArrived = calendar.getTime();
		} catch (ParseException e) {
			try {
				dateArrived = format2.parse(tmp);
			} catch (ParseException e2) {
				dateArrived = format3.parse(tmp);
			}
		}

		tmp = request.getParameter("dateReturn");
		if (tmp == null) {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无返回时间"));
		}
		tmp.replace("：", ":");
		try {
			dateReturn = format.parse(tmp);
			calendar.setTime(dateReturn);
			calendar.set(Calendar.YEAR, year);
			dateReturn = calendar.getTime();
		} catch (ParseException e) {
			try {
				dateReturn = format2.parse(tmp);
			} catch (ParseException e2) {
				dateReturn = format3.parse(tmp);
			}
		}

		tmp = request.getParameter("dateReturned");
		if (tmp == null) {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无到达时间"));
		}
		tmp.replace("：", ":");
		try {
			dateReturned = format.parse(tmp);
			calendar.setTime(dateReturned);
			calendar.set(Calendar.YEAR, year);
			dateReturned = calendar.getTime();
		} catch (ParseException e) {
			try {
				dateReturned = format2.parse(tmp);
			} catch (ParseException e2) {
				dateReturned = format3.parse(tmp);
			}
		}

		double odometerStart = -1;
		double odometerEnd = -1;
		tmp = request.getParameter("odometerStart");
		if (tmp == null) {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无起始里程"));
		}
		odometerStart = Double.parseDouble(tmp);

		tmp = request.getParameter("odometerEnd");
		if (tmp == null) {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无终点里程"));
		}
		odometerEnd = Double.parseDouble(tmp);
		// /////////////////////////////////////////////////////////////////////////////////////////////////

		/*
		 * Outbound cargo information
		 */
		String dId = null;
		List<String> cIds = null;
		List<Image> cImage = null;
		List<Cargo> cargo = null;
		Place cargoSource = null;
		Place cargoDestination = null;
		double finalCargoWeight = -1;

		if (type != OrderType.OreOnly) {
			tmp = request.getParameter("cargoInfo");
			System.out.println("cargo=" + tmp);
			if (tmp == null) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无货物信息"));
			}
			JSONObject cargoInfo = new JSONObject(tmp);
			if (cargoInfo.has("dId")) {
				dId = cargoInfo.getString("dId");
			}

			if (!cargoInfo.has("cargoSource")) {
				// throw new ChannyException(ErrorCode.BAD_ARGUMENT,
				// String.format("无货物信息"));
			} else {
				tmp = cargoInfo.getString("cargoSource");
				cargoSource = placeService.getByName(tmp);
				if (cargoSource == null) {
					// cargoSource = placeService.add(tmp, "系统自动添加");
					cargoSource = new Place();
					cargoSource.setName(tmp);
					cargoSource.setDescription("系统自动添加");
				}
			}

			if (!cargoInfo.has("cargoDestination")) {
				// throw new ChannyException(ErrorCode.BAD_ARGUMENT,
				// String.format("无货物信息"));
			} else {
				tmp = cargoInfo.getString("cargoDestination");
				if (cargoSource != null && cargoSource.getName().equals(tmp)) {
					cargoDestination = cargoSource;
				} else {
					cargoDestination = placeService.getByName(tmp);
				}
				if (cargoDestination == null) {
					// cargoDestination = placeService.add(tmp, "系统自动添加");
					cargoDestination = new Place();
					cargoDestination.setName(tmp);
					cargoDestination.setDescription("系统自动添加");
				}
			}

			if (!cargoInfo.has("cargo")) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无货物明细"));
			}
			JSONArray cargoes = cargoInfo.getJSONArray("cargo");
			cargo = new ArrayList<Cargo>();
			cIds = new ArrayList<String>();
			for (int i = 0; i < cargoes.length(); i++) {
				JSONObject obj = cargoes.getJSONObject(i);
				JSONArray products = obj.getJSONArray("products");

				String cId = obj.getString("cId");
				for (int j = 0; j < products.length(); j++) {
					JSONObject product = products.getJSONObject(j);
					String name = product.getString("name");
					int amount = product.getInt("amount");
					double weight = product.getDouble("weight");

					Product p = productService.getByName(name);
					if (p == null) {
						// p = productService.add(name, "系统自动添加");
						p = new Product();
						p.setName(name);
						p.setDescription("系统自动添加");
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

			if (cargoInfo.has("images")) {
				if (cImage == null) {
					cImage = new ArrayList<Image>();
				}
				JSONArray array = cargoInfo.getJSONArray("images");
				for (int i = 0; i < array.length(); i++) {
					Image image = new Image();
					image.setData(array.getString(i).getBytes());
					image.setDateCreated(new Date());
					image.setType("货运信息");

					cImage.add(image);
				}
			}
		}
		// //////////////////////////////////////////////////////////////////////////////////////////////////

		/*
		 * Inbound ore information
		 */
		String oId = null;
		Ore ore = null;
		Place oreSource = null;
		Place oreDestination = null;
		double oreWeight = -1;
		double finalOreWeight = -1;
		if (type != OrderType.CargoOnly) {
			tmp = request.getParameter("oreInfo");
			System.out.println("ore=" + tmp);
			if (tmp == null) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无矿物信息"));
			}

			JSONObject oreInfo = new JSONObject(tmp);
			if (!oreInfo.has("oId")) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无矿单号"));
			}
			oId = oreInfo.getString("oId");

			if (!oreInfo.has("oreSource")) {
				// throw new ChannyException(ErrorCode.BAD_ARGUMENT,
				// String.format("无装矿点"));
			} else {
				tmp = oreInfo.getString("oreSource");
				if (cargoSource != null && cargoSource.getName().equals(tmp)) {
					oreSource = cargoSource;
				} else if (cargoDestination != null && cargoDestination.getName().equals(tmp)) {
					oreSource = cargoDestination;
				} else {
					oreSource = placeService.getByName(tmp);
				}
				if (oreSource == null) {
					// oreSource = placeService.add(tmp, "系统自动添加");
					oreSource = new Place();
					oreSource.setName(tmp);
					oreSource.setDescription("系统自动添加");
				}
			}

			if (!oreInfo.has("oreDestination")) {
				// throw new ChannyException(ErrorCode.BAD_ARGUMENT,
				// String.format("无装矿点"));
			} else {
				tmp = oreInfo.getString("oreDestination");
				if (cargoSource != null && cargoSource.getName().equals(tmp)) {
					oreDestination = cargoSource;
				} else if (cargoDestination != null && cargoDestination.getName().equals(tmp)) {
					oreDestination = cargoDestination;
				} else if (oreSource != null && oreSource.getName().equals(tmp)) {
					oreDestination = oreSource;
				} else {
					oreDestination = placeService.getByName(tmp);
				}
				if (oreDestination == null) {
					// oreDestination = placeService.add(tmp, "系统自动添加");
					oreDestination = new Place();
					oreDestination.setName(tmp);
					oreDestination.setDescription("系统自动添加");
				}
			}

			if (!oreInfo.has("ore")) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无矿物名称"));
			}
			tmp = oreInfo.getString("ore");
			ore = oreService.getByName(tmp);
			if (ore == null) {
				// ore = oreService.add(tmp, "系统自动添加");
				ore = new Ore();
				ore.setName(tmp);
				ore.setDescription("系统自动添加");
			}

			if (!oreInfo.has("oreWeight")) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无矿重信息"));
			}
			oreWeight = oreInfo.getDouble("oreWeight");

			if (!oreInfo.has("oreFinalWeight")) {
				throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无最终矿重信息"));
			}
			finalOreWeight = oreInfo.getDouble("oreFinalWeight");

			if (oreInfo.has("images")) {
				if (cImage == null) {
					cImage = new ArrayList<Image>();
				}
				JSONArray array = oreInfo.getJSONArray("images");
				for (int i = 0; i < array.length(); i++) {
					Image image = new Image();
					image.setData(array.getString(i).getBytes());
					image.setDateCreated(new Date());
					image.setType("货运信息");

					cImage.add(image);
				}
			}
		}
		// ////////////////////////////////////////////////////////////////////////////////////

		/*
		 * Toll information
		 */
		tmp = request.getParameter("toll");
		if (tmp == null) {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无路费信息"));
		}
		JSONArray tollInfo = new JSONArray(tmp);
		List<Toll> tolls = new ArrayList<Toll>();
		for (int i = 0; i < tollInfo.length(); i++) {
			JSONObject obj = tollInfo.getJSONObject(i);
			Toll toll = new Toll();

			String en = obj.getString("entry");
			TollStation entry = tollStationService.getByName(en);
			if (entry == null) {
				//entry = tollStationService.add(en, "系统自动添加");
				entry = new TollStation();
				entry.setName(en);
				entry.setDescription("系统自动添加");
			}
			toll.setEntry(entry);

			String ex = obj.getString("exit");
			TollStation exit = tollStationService.getByName(ex);
			if (entry != null && entry.getName().equals(ex)) {
				exit = entry;
			}
			if (exit == null) {
				//exit = tollStationService.add(ex, "系统自动添加");
				exit = new TollStation();
				exit.setName(ex);
				exit.setDescription("系统自动添加");
			}
			toll.setExit(exit);

			toll.setAmount(obj.getDouble("amount"));

			if (obj.has("isLoading")) {
				toll.setLoading(true);
			}

			tolls.add(toll);
		}
		// /////////////////////////////////////////////////////////////////////////////////////

		/*
		 * Expenses information
		 */
		List<Expenses> expenses = null;
		tmp = request.getParameter("expensesInfo");
		if (tmp != null) {
			JSONObject expensesInfo = new JSONObject(tmp);
			expenses = new ArrayList<Expenses>();
			if (expensesInfo.has("fine")) {
				JSONArray fines = expensesInfo.getJSONArray("fine");
				for (int i = 0; i < fines.length(); i++) {
					JSONObject obj = fines.getJSONObject(i);
					Expenses fine = new Expenses();
					fine.setType("罚单");
					fine.setDescription(obj.getString("description"));
					fine.setAmount(obj.getDouble("amount"));

					expenses.add(fine);
				}
			}

			if (expensesInfo.has("otherExpenses")) {
				JSONArray otherExpenses = expensesInfo.getJSONArray("otherExpenses");
				for (int i = 0; i < otherExpenses.length(); i++) {
					JSONObject obj = otherExpenses.getJSONObject(i);
					Expenses e = new Expenses();
					e.setType("其余费用");
					e.setDescription(obj.getString("description"));
					e.setAmount(obj.getDouble("amount"));

					expenses.add(e);
				}
			}

			if (expensesInfo.has("images")) {
				if (cImage == null) {
					cImage = new ArrayList<Image>();
				}
				JSONArray array = expensesInfo.getJSONArray("images");
				for (int i = 0; i < array.length(); i++) {
					Image image = new Image();
					image.setData(array.getString(i).getBytes());
					image.setDateCreated(new Date());
					image.setType("费用信息");

					cImage.add(image);
				}
			}
		}

		tmp = request.getParameter("fuelUsed");
		if (tmp == null) {
			throw new ChannyException(ErrorCode.BAD_ARGUMENT, String.format("无油量信息"));
		}
		double fuelUsed = Double.parseDouble(tmp);

		orderService.makeup(motorcade, driver, truck, client, type, dId, cIds, cImage, cargo, cargoSource, cargoDestination, dateDeparted,
				dateArrived, oId, ore, oreSource, oreDestination, dateReturn, dateReturned, finalCargoWeight, oreWeight, finalOreWeight,
				odometerStart, odometerEnd, expenses, tolls, fuelUsed);
		return new JsonResponse(ErrorCode.OK).generate();
	}

	@RequestMapping(value = "/order/query", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String query(@RequestParam("action") String action, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
			HttpServletRequest request) throws JSONException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.OrderQuery) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HibernateUtil.ClassKey, Order.class);

		if (request.getParameter("status") != null) {
			OrderStatus status = OrderStatus.valueOf(request.getParameter("status"));
			filter.put("status", status);
		}

		if (request.getParameter("motorcade") != null) {
			String key = request.getParameter("motorcade");
			Map<String, Object> motorcadeFilter = new HashMap<String, Object>();
			motorcadeFilter.put(HibernateUtil.ClassKey, Motorcade.class);
			motorcadeFilter.put("name", key);

			Map<String, Object> nestedFilter = new HashMap<String, Object>();
			nestedFilter.put(HibernateUtil.ClassKey, Truck.class);
			nestedFilter.put("motorcade", motorcadeFilter);
			filter.put("truck", nestedFilter);
		}

		if (request.getParameter("plate") != null) {
			String key = request.getParameter("plate");
			Map<String, Object> nestedFilter = new HashMap<String, Object>();
			nestedFilter.put(HibernateUtil.ClassKey, Truck.class);
			nestedFilter.put("plate", key);
			filter.put("truck", nestedFilter);
		}

		if (request.getParameter("driver") != null) {
			String key = request.getParameter("driver");
			Map<String, Object> nestedFilter = new HashMap<String, Object>();
			nestedFilter.put(HibernateUtil.ClassKey, Driver.class);
			nestedFilter.put("name", key);
			filter.put("driver", nestedFilter);
		}

		if (request.getParameter("client") != null) {
			String key = request.getParameter("client");
			Map<String, Object> nestedFilter = new HashMap<String, Object>();
			nestedFilter.put(HibernateUtil.ClassKey, Client.class);
			nestedFilter.put("name", key);
			filter.put("client", nestedFilter);
		}

		if (request.getParameter("dId") != null) {
			String key = request.getParameter("dId");
			if (key.equals("无")) {
				key = null;
			}
			filter.put("dId", key);
		}

		if (request.getParameter("cId") != null) {
			String key = request.getParameter("cId");
			filter.put("cId", key);
		}

		if (request.getParameter("oId") != null) {
			String key = request.getParameter("oId");
			if (key.equals("无")) {
				key = null;
			}
			filter.put("oId", key);
		}

		if (request.getParameter("dateDeparted") != null) {
			String key = request.getParameter("dateDeparted");
			if (key.equals("未出发")) {
				key = null;
			}
			filter.put("dateDeparted", key);
		}

		JSONObject data = orderService.query(page, pageSize, filter);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/order/detail", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String detail(@RequestParam("action") String action, HttpServletRequest request, @RequestParam("type") String type,
			@RequestParam("id") long id) throws JSONException {
		Order order = orderService.getById(id);
		if (order == null) {
			return new JsonResponse(ErrorCode.OBJECT_NOT_EXISTED, String.format("运单ID %ld", id)).generate();
		}
		JSONObject data = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateDeparted = "未出发";
		if (order.getDateDeparted() != null) {
			dateDeparted = format.format(order.getDateDeparted());
		}
		String dateArrived = "未到站";
		if (order.getDateDeparted() != null) {
			dateArrived = format.format(order.getDateDeparted());
		}
		String dateReturn = "未返回";
		if (order.getDateDeparted() != null) {
			dateReturn = format.format(order.getDateDeparted());
		}
		String dateReturned = "未到达";
		if (order.getDateDeparted() != null) {
			dateReturned = format.format(order.getDateDeparted());
		}

		data.put("id", order.getId());
		data.put("motorcade", order.getTruck().getMotorcade().getName());
		data.put("truck", order.getTruck().getPlate());
		data.put("driver", order.getDriver().getName());
		data.put("client", order.getClient().getName());
		data.put("type", order.getOrderType().getDescription());
		data.put("dateDeparted", dateDeparted);
		data.put("dateArrived", dateArrived);
		data.put("dateReturn", dateReturn);
		data.put("dateReturned", dateReturned);
		data.put("status", order.getStatus().getDesciption());

		if (order.getdId() == null) {
			data.put("dId", "无");
		} else {
			data.put("dId", order.getdId());
		}

		if (order.getcId() == null) {
			data.put("cargoInfo", "无");
		} else {
			data.put("cargoSource", order.getCargoSource().getName());
			data.put("cargoDestination", order.getCargoDestination().getName());

			JSONArray array = new JSONArray();
			List<Cargo> list = order.getCargo();
			for (Cargo cargo : list) {
				JSONObject obj = new JSONObject();
				obj.put("cId", cargo.getcId());
				obj.put("product", cargo.getProduct().getName());
				obj.put("amount", cargo.getAmount());
				obj.put("weight", cargo.getWeight());

				array.put(obj);
			}
			data.put("cargoInfo", array);
		}

		if (order.getoId() == null) {
			data.put("oreInfo", "无");
		} else {
			JSONObject obj = new JSONObject();
			obj.put("oId", order.getoId());
			obj.put("ore", order.getOre().getName());
			obj.put("weight", order.getOreWeight());
			obj.put("finalWeight", order.getFinalOreWeight());
			obj.put("oreSource", order.getOreSource().getName());
			// obj.put("oreDestination", order.getOreDestination().getName());

			data.put("oreInfo", obj);
		}

		data.put("odometerStart", order.getOdometerStart());
		data.put("odometerEnd", order.getOdometerEnd());
		data.put("distance", order.getDistance());
		data.put("fuelUsed", order.getFuelUsed());

		if (order.getTolls() == null) {
			data.put("tollInfo", "暂无");
		} else {
			JSONArray array = new JSONArray();
			List<Toll> list = order.getTolls();
			for (Toll toll : list) {
				JSONObject obj = new JSONObject();
				obj.put("entry", toll.getEntry().getName());
				obj.put("exit", toll.getExit().getName());
				obj.put("amount", toll.getAmount());

				array.put(obj);
			}
			data.put("tollInfo", array);
		}

		if (order.getExpenses() == null) {
			data.put("expensesInfo", "无");
		} else {
			JSONArray array = new JSONArray();
			List<Expenses> list = order.getExpenses();
			for (Expenses expenses : list) {
				JSONObject obj = new JSONObject();
				obj.put("type", expenses.getType());
				obj.put("description", expenses.getDescription());
				obj.put("amount", expenses.getAmount());

				array.put(obj);
			}
			data.put("expensesInfo", array);
		}

		JSONArray array = new JSONArray();
		if (order.getImage() != null) {
			for (Image image : order.getImage()) {
				JSONObject obj = new JSONObject();
				obj.put("type", image.getType());
				obj.put("base64", new String(image.getData()));

				array.put(obj);
			}
		}
		data.put("image", array);

		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/order/detail", method = RequestMethod.GET)
	public String detail() {
		return "order/detail";
	}
}