package channy.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class StringUtil {
	public static String join(String[] array, String separator) {
		String result = "";
		for (String s : array) {
			if (result != "") {
				result += separator;
			}

			result += s;
		}

		return result;
	}

	public static String join(List<String> array, String separator) {
		String result = "";
		for (String s : array) {
			if (result != "") {
				result += separator;
			}

			result += s;
		}

		return result;
	}

	public static String md5(String text) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] array = md.digest(text.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString().toLowerCase();
	}
}
