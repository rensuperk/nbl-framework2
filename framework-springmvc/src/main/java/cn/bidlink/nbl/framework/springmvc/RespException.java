package cn.bidlink.nbl.framework.springmvc;

public class RespException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public RespException(String message) {
		super(message);
	}
}