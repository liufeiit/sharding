package com.matrix.swan.mvc.component;

import com.matrix.swan.mvc.common.WebModule;

/**
 * 自定义扩展的模板Module
 * <p>
 * 用于直接解析并渲染一个模板，作为一个独立的可复用的模块。
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年10月13日 上午10:03:02
 */
public interface TemplateModule extends WebModule {
	/**
	 * 为当前的模板设置参数.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	TemplateModule setQueryData(String key, Object value);
}