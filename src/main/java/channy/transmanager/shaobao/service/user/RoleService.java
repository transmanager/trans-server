package channy.transmanager.shaobao.service.user;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import channy.transmanager.shaobao.data.QueryResult;
import channy.transmanager.shaobao.data.user.RoleDao;
import channy.transmanager.shaobao.feature.Action;
import channy.transmanager.shaobao.feature.Module;
import channy.transmanager.shaobao.feature.Page;
import channy.transmanager.shaobao.model.user.Role;
import channy.transmanager.shaobao.service.ServiceInterface;
import channy.util.ChannyException;

public class RoleService implements ServiceInterface<Role> {
	private RoleDao dao = new RoleDao();

	public Role add(String name, String description, Set<Module> grantedModules, Set<Page> grantedPages, Set<Action> grantedActions,
			boolean isEditable) {
		Role role = new Role();
		role.setName(name);
		role.setDescription(description);
		role.setGrantedModules(grantedModules);
		role.setGrantedPages(grantedPages);
		role.setGrantedActions(grantedActions);
		role.setEditable(isEditable);
		dao.add(role);

		return role;
	}

	public Role getById(long id) {
		return dao.getById(id);
	}

	public void update(Role entity) {
		dao.update(entity);
	}

	public void remove(Role entity) {
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
		QueryResult<Role> result = dao.query(page, pageSize, filter);

		List<Role> list = result.getData();

		JSONObject data = new JSONObject();
		data.put("total", result.getTotal());
		data.put("match", result.getMatch());

		JSONArray array = new JSONArray();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (Role role : list) {
			JSONObject obj = new JSONObject();
			obj.put("id", role.getId());
			if (role.getName().equals("超级用户")) {
				obj.put("editable", false);
			} else {
				obj.put("editable", true);
			}
			obj.put("name", role.getName());
			obj.put("dateCreated", format.format(role.getDateCreated()));
			array.put(obj);
		}

		data.put("data", array);

		return data;
	}
	
	public Role addClient() {
		return dao.addClient();
	}

	public JSONObject select(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Role getByName(String name) {
		return dao.getByName(name);
	}

	public void importRoles() {
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

		add("超级用户", "", grantedModules, grantedPages, grantedActions, false);
		add("长途车驾驶员", "", grantedModules, grantedPages, grantedActions, true);
		add("中转车驾驶员", "", grantedModules, grantedPages, grantedActions, true);
		add("总经理助理", "", grantedModules, grantedPages, grantedActions, true);
		add("副总经理", "", grantedModules, grantedPages, grantedActions, true);
		add("总调度", "", grantedModules, grantedPages, grantedActions, true);
		add("值班调度", "", grantedModules, grantedPages, grantedActions, true);
		add("文员", "", grantedModules, grantedPages, grantedActions, true);
		add("修理工", "", grantedModules, grantedPages, grantedActions, true);
		add("加油工", "", grantedModules, grantedPages, grantedActions, true);
		add("门卫", "", grantedModules, grantedPages, grantedActions, true);
	}

	public static void main(String[] args) {
		RoleService service = new RoleService();
		service.importRoles();
		Role role = service.getById(1);
		for (Module module : role.getGrantedModules()) {
			System.out.println(module.getDescription());
		}
	}
}
