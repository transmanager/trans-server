package channy.transmanager.shaobao.model.order;

public enum OrderStatus {
	New("新建"),
	WaitingCargo("等待装货"),
	LoadingCargo("正在装货"),
	Outbound("去程途中"),
	UnloadingCargo("正在卸货"),
	WaitingOre("等待装矿"),
	LoadingOre("正在装矿"),
	Inbound("回程途中"),
	UnloadingOre("正在卸矿"),
	CargoVerificationPending("等待货运审核"),
	ExpensesVerificationPending("等待运费审核"),
	ClearancePending("等待结算"),

	CargoVerificationFailed("货运审核失败"),
	ExpensesVerificationFailed("运费审核失败"),
	NotClear("结算失败"),
	
	/*
	 * Add more states before 'Closed', or some pages may NOT
	 * display correctly. 
	 */
	
	////////////////////////////////////////////////////////////////////////
	Closed("已完成"),
	;
	
	private OrderStatus(String desciption) {
		this.desciption = desciption;
	}
	
	private String desciption = "";

	public String getDesciption() {
		return desciption;
	}
}
