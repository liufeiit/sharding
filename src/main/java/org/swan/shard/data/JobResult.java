package org.swan.shard.data;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年3月3日 下午2:22:55
 */
public class JobResult {
	private boolean success;
	private String msg;

	public JobResult() {
		this(false);
	}

	public JobResult(boolean success) {
		super();
		this.success = success;
	}

	public JobResult(boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}

	@Override
	public String toString() {
		if(success) {
			return "success";
		}
		return "Job Error : " + (msg == null ? "Null Error Message." : msg);
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
}