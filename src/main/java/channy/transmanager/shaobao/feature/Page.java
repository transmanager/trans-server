package channy.transmanager.shaobao.feature;

public enum Page {
	Home(Module.System, "系统信息", "system/home"),
	Motorcade(Module.System, "车队管理", "system/motorcade"),
	Vehicle(Module.System, "车辆管理", "system/vehicle"),
	Client(Module.System, "客户管理", "system/client"),
	Cargo(Module.System, "货物管理", "system/cargo"),
	Ore(Module.System, "矿石管理", "system/ore"),
	Expenses(Module.System, "支出管理", "system/expenses"),
	Place(Module.System, "地点管理", "system/place"),
	Product(Module.System, "货物管理", "system/product"),
	
	User(Module.User, "用户管理", "user"),
	Role(Module.User, "角色管理", "role"),
	
	SelfSchedule(Module.Order, "自提单调度", "order/self"),
	ClientSchedule(Module.Order, "物流单调度", "order/client"),
	Order(Module.Order, "运单管理", "order"),
	
	Log(Module.Event, "日志", "log"),
	Abnormality(Module.Event, "异常", "abnormality"),
	
	Status(Module.Mobile, "状态", "status"),
	;
	
	private Page(Module parent, String description, String link) {
		this.parent = parent;
		this.description = description;
		this.link = link;
	}
	
	private Module parent = null;
	private String description = "";
	private String link = "";
	
	public Module getParent() {
		return parent;
	}
	public String getDescription() {
		return description;
	}
	public String getLink() {
		return link;
	}
	
	public Page getByLink(String link) {
		for (Page p : Page.values()) {
			if (p.getLink() == link) {
				return p;
			}
		}
		
		return null;
	}
}
