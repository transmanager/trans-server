package channy.transmanager.shaobao.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import channy.transmanager.shaobao.feature.Module;
import channy.transmanager.shaobao.feature.Page;
import channy.transmanager.shaobao.service.user.RoleService;
import channy.util.ChannyException;
import channy.util.ErrorCode;
import channy.util.JsonResponse;

@Controller
public class RoleController {

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	private RoleService roleService = new RoleService();

	@RequestMapping(value = "/role/select", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String select(@RequestParam("action") String action, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
			@RequestParam("q") String term, HttpServletRequest request) throws JSONException, XPathExpressionException, SAXException, IOException,
			ParserConfigurationException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.RoleQuery) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("name", term);
		JSONObject data = roleService.select(page, pageSize, filter);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/role/query", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String query(@RequestParam("action") String action, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
			HttpServletRequest request) throws JSONException, XPathExpressionException, SAXException, IOException, ParserConfigurationException,
			ChannyException {
		if (Action.valueOf(Action.class, action) != Action.RoleQuery) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		Map<String, Object> filter = new HashMap<String, Object>();

		String role = request.getParameter("name");
		if (role != null) {
			filter.put("name", role);
		}

		String dateCreated = request.getParameter("dateCreated");
		if (dateCreated != null) {
			filter.put("dateCreated", dateCreated);
		}

		JSONObject data = roleService.query(page, pageSize, filter);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/role", method = RequestMethod.GET)
	public String role() {
		return "user/role";
	}

	@RequestMapping(value = "/role/dialog", method = RequestMethod.GET)
	public String role_dialog() {
		return "user/role-dialog";
	}

	@RequestMapping(value = "/role/add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String add(@RequestParam("action") String action, @RequestParam("name") String name, HttpServletRequest request)
			throws ChannyException, JSONException {
		if (Action.valueOf(Action.class, action) != Action.RoleAdd) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		String description = null;
		if (request.getParameter("description") != null) {
			description = request.getParameter("description");
		}

		String[] privileges = request.getParameterValues("privileges[]");
		if (privileges == null) {
			return new JsonResponse(ErrorCode.BAD_ARGUMENT, "无效的权限列表").generate();
		}

		Set<Module> modules = new HashSet<Module>();
		Set<Page> pages = new HashSet<Page>();
		Set<Action> actions = new HashSet<Action>();
		for (String privilege : privileges) {
			try {
				Action a = Action.valueOf(privilege);
				actions.add(a);
				Page page = a.getParent();
				if (page != null) {
					pages.add(a.getParent());
					Module module = page.getParent();
					if (module != null) {
						modules.add(module);
					}
				}
			} catch (IllegalArgumentException e) {
				continue;
			}
		}
		JSONObject data = roleService.add(name, description, modules, pages, actions, true);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}

	@RequestMapping(value = "/role/remove", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String remove(@RequestParam("action") String action, @RequestParam("id") long id, HttpServletRequest request)
			throws ChannyException, JSONException {
		if (Action.valueOf(Action.class, action) != Action.RoleRemove) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}

		roleService.removeById(id);
		return new JsonResponse(ErrorCode.OK).generate();
	}
}
