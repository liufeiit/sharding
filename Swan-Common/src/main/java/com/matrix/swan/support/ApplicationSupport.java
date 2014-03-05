package com.matrix.swan.support;

import org.springframework.context.support.ApplicationObjectSupport;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年12月30日 上午11:12:43
 */
public class ApplicationSupport extends ApplicationObjectSupport {

	@Override
	protected boolean isContextRequired() {
		return true;
	}
	
	protected <T> T getBean(Class<T> requiredType) {
		return getApplicationContext().getBean(requiredType);
	}
	
	protected <T> T getBean(String name, Class<T> requiredType) {
		return getApplicationContext().getBean(name, requiredType);
	}
}