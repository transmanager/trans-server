package channy.transmanager.shaobao.controller;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import channy.transmanager.shaobao.data.TokenDao;
import channy.transmanager.shaobao.feature.Action;
import channy.transmanager.shaobao.model.Image;
import channy.transmanager.shaobao.model.Token;
import channy.transmanager.shaobao.model.order.Order;
import channy.transmanager.shaobao.model.user.Driver;
import channy.transmanager.shaobao.model.user.User;
import channy.transmanager.shaobao.service.OrderService;
import channy.transmanager.shaobao.service.TokenService;
import channy.transmanager.shaobao.service.user.DriverService;
import channy.transmanager.shaobao.service.user.UserService;
import channy.util.ChannyException;
import channy.util.ErrorCode;
import channy.util.HibernateUtil;
import channy.util.JsonResponse;
import channy.util.Sha1;
import channy.util.StringUtil;

@Controller
public class MobileController {
	private DriverService driverService = new DriverService();
	private UserService userService = new UserService();
	private OrderService orderService = new OrderService();
	private TokenService tokenService = new TokenService();

	private static final Logger logger = LoggerFactory.getLogger(MobileController.class);

	@RequestMapping(value = "/mobile/auth", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String authenticate(@RequestParam("action") String action, @RequestParam("employeeId") String employeeId,
			@RequestParam("password") String password, HttpServletRequest request) throws JSONException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.MobileAuthenticate) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		ErrorCode code = userService.auth(employeeId, password);
		if (!code.isOK()) {
			return new JsonResponse(code).generate();
		}

		Session session = HibernateUtil.getCurrentSession();
		session.beginTransaction();
		User user = userService.getDetailByEmployeeId(employeeId, session);
		user.getRole().getName();
		session.getTransaction().commit();
		String uuid = UUID.randomUUID().toString();
		if (TokenDao.exists(user)) {
			Token token = TokenDao.getByUser(user);
			token.setToken(uuid);
			TokenDao.update(token);
		} else {
			TokenDao.add(uuid, user);
		}
		JSONObject data = new JSONObject();
		data.put("name", user.getName());
		data.put("role", user.getRole().getName());
		data.put("token", uuid);
		return new JsonResponse(code, data).generate();
	}

	@RequestMapping(value = "/mobile/changePassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String changePassword(@RequestParam("action") String action, @RequestParam("employeeId") String employeeId,
			@RequestParam("old") String op, @RequestParam("new") String np, @RequestParam("token") String token, HttpServletRequest request)
			throws JSONException {
		User user = userService.getByEmployeeId(employeeId);
		ErrorCode code = TokenDao.authenticate(user, token);
		if (!code.isOK()) {
			return new JsonResponse(code).generate();
		}

		if (Action.valueOf(Action.class, action) != Action.MobileChangePassword) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		code = userService.auth(employeeId, op);
		if (!code.isOK()) {
			return new JsonResponse(code).generate();
		}

		user.setPassword(Sha1.encode(np));
		userService.update(user);

		return new JsonResponse(ErrorCode.OK).generate();
	}

	@RequestMapping(value = "/mobile/queryStatus", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String queryStatus(@RequestParam("action") String action, @RequestParam("employeeId") String employeeId,
			@RequestParam("token") String token, HttpServletRequest request) throws JSONException {

		Session session = HibernateUtil.getCurrentSession();
		session.beginTransaction();
		try {
			User user = userService.getDetailByEmployeeId(employeeId, session);
			ErrorCode code = TokenDao.authenticate(user, token);
			if (!code.isOK()) {
				return new JsonResponse(code).generate();
			}

			if (Action.valueOf(Action.class, action) != Action.MobileQueryStatus) {
				return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
			}

			JSONObject data = new JSONObject();
			if (user.getRole().getName().contains("驾驶员")) {
				Driver driver = (Driver) user;
				data.put("cargoDelivered", driver.getCargoDelivered());
			}

			return new JsonResponse(ErrorCode.OK, data).generate();
		} finally {
			session.getTransaction().commit();
		}
	}

	@RequestMapping(value = "/mobile/sync", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String sync(@RequestParam("action") String action, @RequestParam("employeeId") String employeeId,
			@RequestParam("token") String token, HttpServletRequest request) throws JSONException, ChannyException {
		Session session = HibernateUtil.getCurrentSession();
		session.beginTransaction();
		User user = userService.getDetailByEmployeeId(employeeId, session);
		user.getRole().getName();
		session.getTransaction().commit();
		ErrorCode code = TokenDao.authenticate(user, token);
		if (!code.isOK()) {
			return new JsonResponse(code).generate();
		}

		if (Action.valueOf(Action.class, action) != Action.MobileSync) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		JSONObject data = new JSONObject();
		if (user.getRole().getName().contains("驾驶员")) {
			Driver driver = (Driver) user;
			data = driverService.sync(driver);
		}

		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/mobile/updateOrderStatus", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String updateOrderStatus(@RequestParam("action") String action, @RequestParam("employeeId") String employeeId,
			@RequestParam("token") String token, @RequestParam("orderId") String orderId, HttpServletRequest request) throws JSONException,
			ChannyException {
		User user = userService.getByEmployeeId(employeeId);
		ErrorCode code = TokenDao.authenticate(user, token);
		if (!code.isOK()) {
			return new JsonResponse(code).generate();
		}

		if (Action.valueOf(Action.class, action) != Action.MobileUpdateOrderStatus) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		System.out.println(orderId);
		Order order = orderService.getById(Long.parseLong(orderId));
		if (order == null) {
			return new JsonResponse(String.format("运单%d不存在", orderId)).generate();
		}

		if (!order.getDriver().getEmployeeId().equals(employeeId)) {
			return new JsonResponse(String.format("不能更新别人的运单", orderId)).generate();
		}

		JSONObject data = orderService.updateStatus(order);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/mobile/image/upload/init", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String imageUploadInit(@RequestParam("action") String action, @RequestParam("employeeId") String employeeId,
			@RequestParam("token") String token, @RequestParam("orderId") String orderId, @RequestParam("x") String x, @RequestParam("y") String y,
			@RequestParam("description") String description, @RequestParam("dateTaken") String dateTaken, HttpServletRequest request)
			throws JSONException, ParseException {
		ErrorCode code = tokenService.auth(employeeId, token);
		if (!code.isOK()) {
			return new JsonResponse(code).generate();
		}

		if (Action.valueOf(Action.class, action) != Action.MobileImageUploadInit) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		Session session = HibernateUtil.getCurrentSession();
		session.beginTransaction();
		Order order = orderService.getDetailById(Long.parseLong(orderId), session);
		List<Image> images = null;
		try {
			if (order == null) {
				return new JsonResponse(String.format("运单%s不存在", orderId)).generate();
			}

			if (!order.getDriver().getEmployeeId().equals(employeeId)) {
				return new JsonResponse(String.format("不能修改别人的运单", orderId)).generate();
			}

			images = order.getImage();
			orderService.removeExpired(images);
			if (images == null) {
				images = new ArrayList<Image>();
			}

			String uploadId = UUID.randomUUID().toString();
			Image image = new Image();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			image.setAuthor(order.getDriver());
			image.setDescription(description);
			image.setLongitude(Double.parseDouble(x));
			image.setLatitude(Double.parseDouble(y));
			image.setDateTaken(format.parse(dateTaken));
			image.setUploadId(uploadId);
			image.setLastModified(new Date());

			images.add(image);
			// order.setImage(images);

			// orderService.update(order);

			JSONObject data = new JSONObject();
			data.put("uploadId", uploadId);
			return new JsonResponse(ErrorCode.OK, data).generate();
		} finally {
			session.getTransaction().commit();
		}
	}

	@RequestMapping(value = "/mobile/image/upload/parts", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String imageUpload(@RequestParam("action") String action, @RequestParam("employeeId") String employeeId,
			@RequestParam("token") String token, @RequestParam("orderId") String orderId, @RequestParam("uploadId") String uploadId,
			@RequestParam("payload") String payload, @RequestParam("offset") String offset, HttpServletRequest request) throws JSONException {
		ErrorCode code = tokenService.auth(employeeId, token);
		if (!code.isOK()) {
			return new JsonResponse(code).generate();
		}

		if (Action.valueOf(Action.class, action) != Action.MobileImageUploadParts) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		// Order order = orderService.getById(Long.parseLong(orderId));
		Session session = HibernateUtil.getCurrentSession();
		session.beginTransaction();
		Order order = orderService.getDetailById(Long.parseLong(orderId), session);
		if (order == null) {
			session.getTransaction().commit();
			return new JsonResponse(String.format("运单%d不存在", orderId)).generate();
		}

		List<Image> images = null;
		try {
			if (!order.getDriver().getEmployeeId().equals(employeeId)) {
				return new JsonResponse("不能修改别人的运单").generate();
			}

			images = order.getImage();
			if (images == null) {
				return new JsonResponse("无效的请求，请先初始化上传进程").generate();
			}
			orderService.removeExpired(images);

			Image target = null;
			for (Image image : images) {
				if (image.getUploadId().equals(uploadId)) {
					target = image;
					break;
				}
			}

			if (target == null) {
				return new JsonResponse("无效的上传ID").generate();
			}

			String data = null;
			if (target.getData() != null) {
				data = new String(target.getData());
				int os = Integer.parseInt(offset);
				if (data.length() < os) {
					return new JsonResponse("无效的偏移量").generate();
				}

				data = data.substring(0, os);
				data += payload;
			} else {
				data = payload;
			}
			target.setData(data.getBytes());
			//target.setLastModified(new Date());
		} finally {
			session.getTransaction().commit();
		}
		orderService.update(order);
		return new JsonResponse(ErrorCode.OK).generate();
	}

	@RequestMapping(value = "/mobile/image/upload/complete", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String imageUploadComplete(@RequestParam("action") String action, @RequestParam("employeeId") String employeeId,
			@RequestParam("token") String token, @RequestParam("orderId") String orderId, @RequestParam("md5") String md5,
			@RequestParam("uploadId") String uploadId, HttpServletRequest request) throws NoSuchAlgorithmException {
		ErrorCode code = tokenService.auth(employeeId, token);
		if (!code.isOK()) {
			return new JsonResponse(code).generate();
		}

		if (Action.valueOf(Action.class, action) != Action.MobileImageUploadComplete) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		Session session = HibernateUtil.getCurrentSession();
		session.beginTransaction();
		Order order = orderService.getDetailById(Long.parseLong(orderId), session);
		List<Image> images = null;
		try {
			if (order == null) {
				return new JsonResponse(String.format("运单%d不存在", orderId)).generate();
			}

			if (!order.getDriver().getEmployeeId().equals(employeeId)) {
				return new JsonResponse("不能修改别人的运单").generate();
			}

			images = order.getImage();
			if (images == null) {
				return new JsonResponse("无效的请求，请先初始化上传进程").generate();
			}
			orderService.removeExpired(images);

			Image target = null;
			for (Image image : images) {
				if (image.getUploadId().equals(uploadId)) {
					target = image;
					break;
				}
			}

			if (target == null) {
				return new JsonResponse("无效的上传ID").generate();
			}

			String localMd5 = StringUtil.md5(new String(target.getData()));
			if (!localMd5.equals(md5)) {
				order.getImage().remove(target);
				if (images.isEmpty()) {
					order.setImage(null);
				}
				orderService.update(order);

				return new JsonResponse("摘要值不匹配，请重新上传").generate();
			}
			target.setReady(true);
		} finally {
			session.getTransaction().commit();
		}

		orderService.update(order);

		return new JsonResponse(ErrorCode.OK).generate();
	}

	@RequestMapping(value = "/mobile/image/add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String imageAdd(@RequestParam("action") String action, @RequestParam("employeeId") String employeeId,
			@RequestParam("token") String token, @RequestParam("orderId") String orderId, @RequestParam("picture") String base64Picture,
			@RequestParam("x") String x, @RequestParam("y") String y, @RequestParam("description") String description,
			@RequestParam("dateTaken") String dateTaken /* yyyy-MM-dd HH:mm:ss */, HttpServletRequest request) throws JSONException, ChannyException,
			ParseException {
		User user = userService.getByEmployeeId(employeeId);
		ErrorCode code = TokenDao.authenticate(user, token);
		if (!code.isOK()) {
			return new JsonResponse(code).generate();
		}

		if (Action.valueOf(Action.class, action) != Action.MobileImageAdd) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		System.out.println("order id=" + orderId);
		Order order = orderService.getById(Long.parseLong(orderId));
		if (order == null) {
			return new JsonResponse(String.format("运单%d不存在", orderId)).generate();
		}

		System.out.println(employeeId);
		System.out.println(order.getDriver().getEmployeeId());
		if (!order.getDriver().getEmployeeId().equals(employeeId)) {
			return new JsonResponse(String.format("不能修改别人的运单", orderId)).generate();
		}

		List<Image> images = order.getImage();
		if (images == null) {
			images = new ArrayList<Image>();
		}

		System.out.println(dateTaken);
		Image image = new Image();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		image.setAuthor(order.getDriver());
		image.setData(base64Picture.getBytes());
		image.setDescription(description);
		image.setLongitude(Double.parseDouble(x));
		image.setLatitude(Double.parseDouble(y));
		image.setDateTaken(format.parse(dateTaken));
		image.setReady(true);
		image.setLastModified(new Date());
		images.add(image);
		order.setImage(images);
		orderService.update(order);
		return new JsonResponse(ErrorCode.OK).generate();
	}

	@RequestMapping(value = "/mobile/image/remove", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String updateOrderPicture(@RequestParam("action") String action, @RequestParam("employeeId") String employeeId,
			@RequestParam("token") String token, @RequestParam("imageId") String imageId, HttpServletRequest request) throws JSONException,
			ChannyException, ParseException {
		// User user = userService.getByEmployeeId(employeeId);
		// ErrorCode code = TokenDao.authenticate(user, token);
		// if (!code.isOK()) {
		// return new JsonResponse(code).generate();
		// }
		//
		// if (Action.valueOf(Action.class, action) != Action.MobileImageAdd) {
		// return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		// }
		//
		// System.out.println("order id=" + orderId);
		// Order order = orderService.getById(Long.parseLong(orderId));
		// if (order == null) {
		// return new JsonResponse(String.format("运单%d不存在",
		// orderId)).generate();
		// }
		//
		// if (!order.getDriver().getEmployeeId().equals(employeeId)) {
		// return new JsonResponse(String.format("不能修改别人的运单",
		// orderId)).generate();
		// }
		return new JsonResponse(ErrorCode.BAD_COMMAND).generate();
	}
}