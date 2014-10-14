package channy.util;

public class ChannyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3693433712800014080L;

	public ChannyException(ErrorCode code) {
		super(code.getDetail());
		this.code = code;
	}

	public ChannyException(ErrorCode code, String message) {
		super(message);
		this.code = code;
	}

	public ChannyException(ErrorCode code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public ErrorCode getErrorCode() {
		return code;
	}

	public ChannyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	private ErrorCode code = null;
}
