package channy.transmanager.shaobao.feature;

public enum Action {
	Login(null, "登录"),
	Logout(null, "退出"),
	
	MotorcadeAdd(Page.Motorcade, "添加车队"),
	MotorcadeRemove(Page.Motorcade, "删除车队"),
	MotorcadeEdit(Page.Motorcade, "编辑车队"),
	MotorcadeQuery(Page.Motorcade, "查看车队"),
	
	UserAdd(Page.User, "添加车队"),
	UserRemove(Page.User, "删除车队"),
	UserEdit(Page.User, "编辑车队"),
	UserQuery(Page.User, "查看车队"),
	UserLock(Page.User, "锁定用户"),
	UserUnlock(Page.User, "解锁用户"),
	UserUpdateStatus(Page.User, "更新用户状态"),
	
	RoleAdd(Page.Role, "添加角色"),
	RoleRemove(Page.Role, "删除角色"),
	RoleEdit(Page.Role, "编辑角色"),
	RoleQuery(Page.Role, "查看角色"),
	
	TruckAdd(Page.Vehicle, "添加车辆"),
	TruckRemove(Page.Vehicle, "删除车辆"),
	TruckEdit(Page.Vehicle, "编辑车辆"),
	TruckQuery(Page.Vehicle, "查看车辆"),
	TruckUpdateStatus(Page.Vehicle, "更新车辆状态"),
	
	AbnormalityReport(Page.Abnormality, "报告异常"),
	AbnormalityQuery(Page.Abnormality, "查看异常"),
	
	ExpensesAdd(Page.Expenses, "添加支出"),
	ExpensesRemove(Page.Expenses, "删除支出"),
	ExpensesEdit(Page.Expenses, "编辑支出"),
	ExpensesQuery(Page.Expenses, "查看支出"),
	
	OreAdd(Page.Ore, "添加矿石"),
	OreRemove(Page.Ore, "删除矿石"),
	OreEdit(Page.Ore, "编辑矿石"),
	OreQuery(Page.Ore, "查看矿石"),
	
	PlaceAdd(Page.Place, "添加地点"),
	PlaceRemove(Page.Place, "删除地点"),
	PlaceEdit(Page.Place, "编辑地点"),
	PlaceQuery(Page.Place, "查看地点"),
	
	ProductAdd(Page.Product, "添加货物"),
	ProductRemove(Page.Product, "删除货物"),
	ProductEdit(Page.Product, "编辑货物"),
	ProductQuery(Page.Product, "查看货物"),
	
	TollStationAdd(Page.Product, "添加收费站"),
	TollStationRemove(Page.Product, "删除收费站"),
	TollStationEdit(Page.Product, "编辑收费站"),
	TollStationQuery(Page.Product, "查看收费站"),
	
	OrderSchedule(Page.Order, "调度运单"),
	OrderMakeup(Page.Order, "补录运单"),
	OrderRemove(Page.Order, "删除运单"),
	OrderEdit(Page.Order, "编辑运单"),
	OrderQuery(Page.Order, "查看运单"),
	OrderUpdateStatus(Page.Order, "更新运单状态"),
	OrderRefreshSchedule(Page.Order, "刷新调运信息"),
	OrderShowDetail(Page.Order, "查看运单详情"),
	
	MobileAuthenticate(null, "移动端身份验证"),
	MobileQueryStatus(null, "查询个人状态"),
	MobileChangePassword(null, "修改密码"),
	MobileUpdateOrder(null, "更新运单"),
	MobileUpdateOrderStatus(null, "更新运单状态"),
	MobileUpdateTruck(null, "更新车辆状态"),
	MobileSync(null, "同步任务"),
	
	MobileImageAdd(null, "上传图片"),
	MobileImageUploadInit(null, "初始化图片上传"),
	MobileImageUploadParts(null, "分段上传图片"),
	MobileImageUploadComplete(null, "完成图片上传"),
	MobileImageRemove(null, "删除图片"),
	;
	
	private Action(Page parent, String description) {
		this.parent = parent;
		this.description = description;
	}
	
	private Page parent = null;
	private String description = "";
	
	public Page getParent() {
		return parent;
	}
	public String getDescription() {
		return description;
	}
}
