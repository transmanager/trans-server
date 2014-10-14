package channy.transmanager.shaobao.model.user;

public enum UserStatus {
	Idle("空闲"),
	OnVacation("休假"),
	Travelling("出差"),
	Delivering("送货"),
	Normal("普通"),
	;
	
	private UserStatus(String description) {
		this.description = description;
	}
	
	private String description = "";

	public String getDescription() {
		return description;
	}
}
