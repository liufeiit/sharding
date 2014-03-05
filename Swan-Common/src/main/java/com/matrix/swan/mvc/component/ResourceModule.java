package com.matrix.swan.mvc.component;

import com.matrix.swan.mvc.common.WebModule;

/**
 * 资源访问Module。
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年10月13日 上午10:02:32
 */
public interface ResourceModule extends WebModule {
	ResourceModule setProtocol(String protocol);

	ResourceModule setHost(String host);

	ResourceModule setPort(int port);

	ResourceModule setContext(String context);

	ResourceModule setPrefix(String prefix);
}