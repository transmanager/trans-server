package channy.transmanager.shaobao.data.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import channy.transmanager.shaobao.config.Configuration;
import channy.transmanager.shaobao.data.BaseDao;
import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.feature.Action;
import channy.transmanager.shaobao.feature.Module;
import channy.transmanager.shaobao.feature.Page;
import channy.transmanager.shaobao.model.user.Role;
import channy.util.ChannyException;
import channy.util.ErrorCode;
import channy.util.XmlOperator;

public class RoleDao extends BaseDao<Role>  {
	public static boolean roleExists(String name) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		String xpath = String.format("/config/role[@name=%s]", XmlOperator.escape(name));
		ArrayList<String> list = XmlOperator.read(Configuration.Privilege, xpath, "name");
		return list.size() > 0;
	}

	public static Role getRoleByName(String name) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		if (!RoleDao.roleExists(name)) {
			return null;
		}

		/*
		 * built-in
		 */
		String xpath = String.format("/config/role[@name=%s]", XmlOperator.escape(name));
		ArrayList<String> list = XmlOperator.read(Configuration.Privilege, xpath, "built-in");
		boolean isEditable = false;
		if (list.size() > 0) {
			isEditable = true;
		}
		// ///////////////////////////////////////////////////////////////////

		/*
		 * modules
		 */
		xpath = String.format("/config/role[@name=%s]/module", XmlOperator.escape(name));
		list = XmlOperator.read(Configuration.Privilege, xpath, "");
		Set<Module> modules = new HashSet<Module>();
		for (String m : list) {
			try {
				Module module = Module.valueOf(Module.class, m);
				modules.add(module);
			} catch (Exception e) {
			}
		}
		// ///////////////////////////////////////////////////////////////////

		/*
		 * pages
		 */
		xpath = String.format("/config/role[@name=%s]/page", XmlOperator.escape(name));
		list = XmlOperator.read(Configuration.Privilege, xpath, "");
		Set<Page> pages = new HashSet<Page>();
		for (String p : list) {
			try {
				Page page = Page.valueOf(Page.class, p);
				pages.add(page);
			} catch (Exception e) {
			}
		}
		// /////////////////////////////////////////////////////////////////

		/*
		 * actions
		 */
		xpath = String.format("/config/role[@name=%s]/action", XmlOperator.escape(name));
		list = XmlOperator.read(Configuration.Privilege, xpath, "");
		Set<Action> actions = new HashSet<Action>();
		for (String a : list) {
			try {
				Action action = Action.valueOf(Action.class, a);
				actions.add(action);
			} catch (Exception e) {
			}
		}
		// ///////////////////////////////////////////////////////////////

		Role r = new Role();
		r.setName(name);
		r.setEditable(isEditable);
		r.setGrantedModules(modules);
		r.setGrantedPages(pages);
		r.setGrantedActions(actions);
		return r;
	}

	public static void add(String name, Set<Module> grantedModules, Set<Page> grantedPages, Set<Action> grantedActions) throws XPathExpressionException, SAXException,
			IOException, ParserConfigurationException, ChannyException {
		if (RoleDao.roleExists(name)) {
			throw new ChannyException(ErrorCode.USER_EXISTED);
		}

		XmlOperator.append(Configuration.Privilege, "/config", "role", "name", name);

		String xpath = String.format("/config/role[@name=%s]", XmlOperator.escape(name));
		XmlOperator.append(Configuration.Privilege, xpath, "", "built-in", "false");

		for (Module item : grantedModules) {
			XmlOperator.append(Configuration.Privilege, xpath, "module", "", item.toString());
		}

		for (Page item : grantedPages) {
			XmlOperator.append(Configuration.Privilege, xpath, "page", "", item.toString());
		}

		for (Action item : grantedActions) {
			XmlOperator.append(Configuration.Privilege, xpath, "action", "", item.toString());
		}
	}

	@SuppressWarnings("unused")
	private static void addBuiltinRole(String name, Set<Module> grantedModules, Set<Page> grantedPages, Set<Action> grantedActions) throws XPathExpressionException,
			SAXException, IOException, ParserConfigurationException, ChannyException {
		if (RoleDao.roleExists(name)) {
			throw new ChannyException(ErrorCode.USER_EXISTED);
		}

		XmlOperator.append(Configuration.Privilege, "/config", "role", "name", name);

		String xpath = String.format("/config/role[@name=%s]", XmlOperator.escape(name));
		XmlOperator.append(Configuration.Privilege, xpath, "", "built-in", "false");

		for (Module item : grantedModules) {
			XmlOperator.append(Configuration.Privilege, xpath, "module", "", item.toString());
		}

		for (Page item : grantedPages) {
			XmlOperator.append(Configuration.Privilege, xpath, "page", "", item.toString());
		}

		for (Action item : grantedActions) {
			XmlOperator.append(Configuration.Privilege, xpath, "action", "", item.toString());
		}
	}

	public static JSONObject querySelect(int page, int pageSize, String term) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException,
			JSONException, ChannyException {
		JSONObject result = new JSONObject();
		String condition = "";
		String xpath = String.format("/config/role", condition);
		if (term != null && !term.isEmpty()) {
			Map<String, String> filter = new HashMap<String, String>();
			filter.put("name", term);
			condition = XmlOperator.assembleXpathCondition(filter);
			xpath += String.format("[%s]", condition);
		}
		
		xpath += String.format("[@name!='%s']", "超级用户");
		System.out.println(xpath);

		Number total = XmlOperator.read(Configuration.Privilege, String.format("count(%s)", xpath));

		int offset = (page - 1) * pageSize;
		int bound = offset + pageSize;

		if (offset > total.intValue()) {
			offset = (total.intValue() / pageSize - 1) * (page - 1);
			bound = offset + pageSize;
		}

		xpath += String.format("[position() >= %d and position() <= %d]", offset + 1, bound);

		List<String> list = XmlOperator.read(Configuration.Privilege, xpath, "name");
		JSONArray data = new JSONArray();
		for (String name : list) {
			JSONObject obj = new JSONObject();
			obj.put("id", name);
			obj.put("text", name);
			data.put(obj);
		}

		result.put("results", data);
		result.put("total", total.intValue());
		return result;
	}

	public static void main(String[] args) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, ChannyException, JSONException {
		// Role role = RoleDao.getRoleByName("超级用户");
		// System.out.println(role.getGrantedModules().get(0));
		// System.out.println(RoleDao.roleExists("超级用户"));

		// ArrayList<Module> modules = new ArrayList<Module>();
		// ArrayList<Page> pages = new ArrayList<Page>();
		// ArrayList<Action> actions = new ArrayList<Action>();
		//
		// for (Module m : Module.values()) {
		// modules.add(m);
		// }
		//
		// for (Page p : Page.values()) {
		// pages.add(p);
		// }
		//
		// for (Action a : Action.values()) {
		// actions.add(a);
		// }
		// //
		// RoleDao.addBuiltinRole("超级用户", modules, pages, actions);
		//
		// Role role = RoleDao.getRoleByName("超级用户");
		// System.out.println(role.getGrantedModules().get(0));
		// System.out.println(RoleDao.querySelect(0, 10, "lll"));
		Set<Module> grantedModules = new HashSet<Module>();
		Set<Page> grantedPages = new HashSet<Page>();
		Set<Action> grantedActions = new HashSet<Action>();

		for (Module m : Module.values()) {
			grantedModules.add(m);
		}

		for (Page p : Page.values()) {
			grantedPages.add(p);
		}

		for (Action a : Action.values()) {
			grantedActions.add(a);
		}
		
		RoleDao.add("超级用户", grantedModules, grantedPages, grantedActions);
	}

	@Override
	public Role getById(long id) {
		return super.getById(id, Role.class);
	}

	@Override
	public void removeById(long id) throws ChannyException {
		super.removeById(id, Role.class);
	}

	@Override
	public QueryResult<Role> query(int page, int pageSize, Map<String, Object> filter) throws ChannyException {
		return super.query(page, pageSize, filter, Role.class);
	}

	@Override
	public int getCount(Map<String, Object> filter) throws ChannyException {
		return super.getCount(Role.class, filter);
	}
	
	public Role getByName(String name) {
		List<Role> roles = super.getByField("name", name, Role.class);
		if (roles != null) {
			return roles.get(0);
		}
		
		return null;
	}
}
