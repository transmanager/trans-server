package channy.transmanager.shaobao.model.order;

public enum OrderType {
	CargoOnly("单程货"),
	OreOnly("单程矿"),
	RoundTrip("双程")
	;
	private OrderType(String description) {
		this.description = description;
	}
	
	private String description = "";

	public String getDescription() {
		return description;
	}
}
