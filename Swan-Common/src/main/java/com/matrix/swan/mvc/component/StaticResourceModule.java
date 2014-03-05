package com.matrix.swan.mvc.component;

import org.apache.commons.lang.StringUtils;

import com.matrix.swan.mvc.common.WebModule;

/**
 * 静态资源访问控制器。
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年10月13日 上午10:02:08
 */
public class StaticResourceModule extends BaseWebModule {
	private static final String HTTP = "http";
	private String protocol;
	private String host;
	private int port;
	private String context;
	private String prefix;
	
	private String target;
	
	private String server;

	@Override
	protected void init() throws Exception {
		if(protocol == null || protocol.isEmpty()) {
			protocol = HTTP;
		}
		protocol = protocol.toLowerCase();
		if(host == null || host.isEmpty()) {
			logger.error("'host' is Empty!!!");
		}
		context = StringUtils.defaultIfBlank(context, StringUtils.EMPTY);
		prefix = StringUtils.defaultIfBlank(prefix, StringUtils.EMPTY);
		server = protocol + "://" + host + (port <= 0 ? "" : ":" + port) + PATH_VAR + context + PATH_VAR + prefix + PATH_VAR;
	}
	
	@Override
	public WebModule setTarget(Object target) {
		this.target = String.valueOf(target);
		return this;
	}

	@Override
	public String render() {
		return server + target;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public String toString() {
		return render();
	}
}