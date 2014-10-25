package channy.util;

public class Base64Util {
	public static String escape(String base64) {
		return base64.replace("+", "-").replace("/", "_").replace("=", ",");
	}
	
	public static String unescape(String escaped) {
		String result = escaped.replace("-", "+").replace("_", "/").replace(",", "=");
		String prefix = "data:image/jpeg;base64=";
		if (result.contains(prefix)) {
			result = result.replace(prefix, "data:image/jpeg;base64,");
		}
		
		return result;
	}
}
