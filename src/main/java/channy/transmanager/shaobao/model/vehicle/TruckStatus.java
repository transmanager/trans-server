package channy.transmanager.shaobao.model.vehicle;

public enum TruckStatus {
	Idle("空闲"),
	Scheduled("已调度"),
	LoadingCargo("正在装货"),
	UnloadingCargo("正在卸货"),
	Outbound("去程途中"),
	Inbound("回程途中"),
	LoadingOre("正在装矿"),
	UnloadingOre("正在卸矿"),
	Repairing("维修中"),
	Refueling("加油中"),
	;
	
	private TruckStatus(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}

	private String description = "";
}
