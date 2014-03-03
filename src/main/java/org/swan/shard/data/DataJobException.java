package org.swan.shard.data;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年3月3日 下午2:15:55
 */
public class DataJobException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataJobException() {
	}

	public DataJobException(String message) {
		super(message);
	}

	public DataJobException(Throwable cause) {
		super(cause);
	}

	public DataJobException(String message, Throwable cause) {
		super(message, cause);
	}
}