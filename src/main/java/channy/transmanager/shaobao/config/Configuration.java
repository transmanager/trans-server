package channy.transmanager.shaobao.config;

public class Configuration {
	public static String context = "/Users/Channy/Documents/Eclipse/shaobao/src/main/webapp";
	//public static String context = "";
	private static String ConfigHome = context + "/WEB-INF/config";
	public static String Privilege = ConfigHome + "/privileges.xml";
	
	public static void update() {
		ConfigHome = context + "/WEB-INF/config";
		Privilege = ConfigHome + "/privileges.xml";
	}
}
