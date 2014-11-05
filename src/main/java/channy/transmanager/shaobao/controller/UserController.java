package channy.transmanager.shaobao.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

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
import channy.transmanager.shaobao.model.user.Role;
import channy.transmanager.shaobao.model.user.UserStatus;
import channy.transmanager.shaobao.service.user.DriverService;
import channy.transmanager.shaobao.service.user.UserService;
import channy.util.ChannyException;
import channy.util.ErrorCode;
import channy.util.HibernateUtil;
import channy.util.JsonResponse;

@Controller
public class UserController {
	private UserService userService = new UserService();
	private DriverService driverService = new DriverService();

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String user() {
		return "user/user";
	}

	@RequestMapping(value = "/user/query", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String query(@RequestParam("action") String action, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize, HttpServletRequest request)
			throws JSONException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.UserQuery) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		Map<String, Object> filter = new HashMap<String, Object>();
		//filter.put(HibernateUtil.ClassKey, User.class);
		String id = request.getParameter("employeeId");
		if (id != null) {
			filter.put("employeeId", id);
		}

		String name = request.getParameter("name");
		if (name != null) {
			filter.put("name", name);
		}

		String role = request.getParameter("role");
		if (role != null) {
			filter.put("dbRole", role);
		}

		String dateCreated = request.getParameter("dateCreated");
		System.out.println(dateCreated);
		if (dateCreated != null) {
			filter.put("dateCreated", dateCreated);
		}
		
		JSONObject data = userService.query(page, pageSize, filter);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}
	
	@RequestMapping(value = "/user/select", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String select(@RequestParam("action") String action, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize, HttpServletRequest request)
			throws JSONException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.UserQuery) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		Map<String, Object> filter = new HashMap<String, Object>();
		String name = request.getParameter("name");
		if (name != null) {
			filter.put("name", name);
		}

		String role = request.getParameter("role");
		if (role != null) {
			Map<String, Object> nestedFilter = new HashMap<String, Object>();
			nestedFilter.put(HibernateUtil.ClassKey, Role.class);
			nestedFilter.put("name", role);
			filter.put("role", nestedFilter);
		}
		
		String s = request.getParameter("status");
		if (s != null) {
			UserStatus status = UserStatus.valueOf(s);
			filter.put("status", status);
		}

		JSONObject data = userService.select(page - 1, pageSize, filter);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}
	
	@RequestMapping(value = "/driver/select", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String selectDriver(@RequestParam("action") String action, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize, HttpServletRequest request)
			throws JSONException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.UserQuery) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		String name = request.getParameter("name");
		JSONObject data = driverService.getAvailableDrivers(name, page, pageSize);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/user/add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String add(@RequestParam("action") String action, @RequestParam("employeeId") String employeeId, @RequestParam("password") String password,
			@RequestParam("role") String role, HttpServletRequest request) throws JSONException, XPathExpressionException, SAXException, IOException, ParserConfigurationException,
			ChannyException {
		if (Action.valueOf(Action.class, action) != Action.UserAdd) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}
		String name = request.getParameter("name");
		JSONObject data = userService.add(employeeId, name, password, role);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/user/remove", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String remove(@RequestParam("action") String action, @RequestParam("id") long id, HttpServletRequest request) throws JSONException,
			XPathExpressionException, SAXException, IOException, ParserConfigurationException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.UserRemove) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}
		userService.removeById(id);
		return new JsonResponse(ErrorCode.OK).generate();
	}
	
	@RequestMapping(value = "/user/lock", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String lock(@RequestParam("action") String action, @RequestParam("id") long id, HttpServletRequest request) throws JSONException,
			XPathExpressionException, SAXException, IOException, ParserConfigurationException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.UserLock) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}
		userService.lock(id);
		return new JsonResponse(ErrorCode.OK).generate();
	}
	
	@RequestMapping(value = "/user/unlock", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String unlock(@RequestParam("action") String action, @RequestParam("id") long id, HttpServletRequest request) throws JSONException,
			XPathExpressionException, SAXException, IOException, ParserConfigurationException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.UserUnlock) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}
		userService.unlock(id);
		return new JsonResponse(ErrorCode.OK).generate();
	}

	@RequestMapping(value = "/user/dialog", method = RequestMethod.GET)
	public String user_dialog() {
		return "user/user-dialog";
	}
}