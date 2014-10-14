package channy.transmanager.shaobao.model.event;

public enum EventType {
	Order("运单"),
	Exception("异常"),
	Fuel("油料"),
	Repairment("修理"),
	Other("其它"),
	;
	
	private EventType(String description) {
		this.description = description;
	}
	
	private String description = "";

	public String getDescription() {
		return description;
	}
	
}
