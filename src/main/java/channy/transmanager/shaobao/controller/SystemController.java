package channy.transmanager.shaobao.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import channy.transmanager.shaobao.feature.Action;
import channy.transmanager.shaobao.model.Ore;
import channy.transmanager.shaobao.model.Place;
import channy.transmanager.shaobao.model.Product;
import channy.transmanager.shaobao.model.TollStation;
import channy.transmanager.shaobao.model.user.User;
import channy.transmanager.shaobao.model.vehicle.Motorcade;
import channy.transmanager.shaobao.model.vehicle.Truck;
import channy.transmanager.shaobao.service.OreService;
import channy.transmanager.shaobao.service.PlaceService;
import channy.transmanager.shaobao.service.ProductService;
import channy.transmanager.shaobao.service.TollStationService;
import channy.transmanager.shaobao.service.user.UserService;
import channy.transmanager.shaobao.service.vehicle.MotorcadeService;
import channy.transmanager.shaobao.service.vehicle.TruckService;
import channy.util.ChannyException;
import channy.util.ErrorCode;
import channy.util.HibernateUtil;
import channy.util.JsonResponse;

/**
 * Handles requests for the application home page.
 */
@Controller
public class SystemController {
	private UserService userService = new UserService();
	private MotorcadeService motorcadeService = new MotorcadeService();
	private TruckService truckService = new TruckService();
	private PlaceService placeService = new PlaceService();
	private ProductService productService = new ProductService();
	private OreService oreService = new OreService();
	private TollStationService tollStationService = new TollStationService();

	private static final Logger logger = LoggerFactory.getLogger(SystemController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Locale locale, Model model) {
		// logger.info("Welcome home! The client locale is {}.", locale);

		return "index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String login(@RequestParam("id") String id, @RequestParam("password") String password, HttpSession session)
			throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		ErrorCode code = userService.auth(id, password);
		if (!code.isOK()) {
			return new JsonResponse(code).generate();
		}

		Session s = HibernateUtil.getCurrentSession();
		s.beginTransaction();
		User user = userService.getDetailByEmployeeId(id, s);
		s.getTransaction().commit();
		session.setAttribute("currentUser", user);

		JSONObject data = new JSONObject();
		try {
			data.put("url", "main");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		return new ModelAndView("redirect:index");
	}

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main() {
		return "frames/main";
	}

	@RequestMapping(value = "/system/vehicle", method = RequestMethod.GET)
	public String vehicle() {
		return "system/vehicle";
	}

	@RequestMapping(value = "/system/motorcade/select", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String selectMotorcade(@RequestParam("action") String action, @RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize, HttpServletRequest request) throws JSONException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.MotorcadeQuery) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HibernateUtil.ClassKey, Motorcade.class);
		String name = request.getParameter("name");
		if (name != null) {
			filter.put("name", name);
		}

		JSONObject data = motorcadeService.select(page - 1, pageSize, filter);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/system/vehicle/select", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String selectVehicle(@RequestParam("action") String action, @RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize, HttpServletRequest request) throws JSONException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.TruckQuery) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HibernateUtil.ClassKey, Truck.class);
		String name = request.getParameter("name");
		if (name != null) {
			filter.put("plate", name);
		}

		JSONObject data = truckService.select(page - 1, pageSize, filter);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/system/place/select", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String selectPlace(@RequestParam("action") String action, @RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize, HttpServletRequest request) throws JSONException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.PlaceQuery) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HibernateUtil.ClassKey, Place.class);
		String name = request.getParameter("name");
		if (name != null) {
			filter.put("name", name);
		}

		JSONObject data = placeService.select(page - 1, pageSize, filter);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/system/product/select", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String selectProduct(@RequestParam("action") String action, @RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize, HttpServletRequest request) throws JSONException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.ProductQuery) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HibernateUtil.ClassKey, Product.class);
		String name = request.getParameter("name");
		if (name != null) {
			filter.put("name", name);
		}

		JSONObject data = productService.select(page - 1, pageSize, filter);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/system/ore/select", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String selectOre(@RequestParam("action") String action, @RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize, HttpServletRequest request) throws JSONException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.OreQuery) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HibernateUtil.ClassKey, Ore.class);
		String name = request.getParameter("name");
		if (name != null) {
			filter.put("name", name);
		}

		JSONObject data = oreService.select(page - 1, pageSize, filter);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/system/tollstation/select", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String selectTollStation(@RequestParam("action") String action, @RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize, HttpServletRequest request) throws JSONException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.TollStationQuery) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HibernateUtil.ClassKey, TollStation.class);
		String name = request.getParameter("name");
		if (name != null) {
			filter.put("name", name);
		}

		JSONObject data = tollStationService.select(page - 1, pageSize, filter);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}
}
