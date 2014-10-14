package channy.transmanager.shaobao.feature;

public enum Module {
	
	System("系统", false),
	User("用户", false),
	Order("运单", false),
	Event("事件", false),
	
	// Module for end point user
	Mobile("终端", true),
	;
	
	private Module(String description, boolean mobileOnly) {
		this.description = description;
		this.mobileOnly = mobileOnly;
	}
	
	private String description = "";
	private boolean mobileOnly = false;

	public String getDescription() {
		return description;
	}
	
	public boolean isMobileOnly() {
		return mobileOnly;
	}
}
