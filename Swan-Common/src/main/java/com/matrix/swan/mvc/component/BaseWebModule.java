package com.matrix.swan.mvc.component;

import org.springframework.web.context.support.WebApplicationObjectSupport;

import com.matrix.swan.mvc.common.WebModule;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年1月4日 下午11:31:00
 */
public abstract class BaseWebModule extends WebApplicationObjectSupport implements WebModule {

	@Override
	public final void afterPropertiesSet() throws Exception {
		init();
	}
	
	protected void init() throws Exception {
		
	}
}