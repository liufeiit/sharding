package com.matrix.swan.mvc.common;

import org.springframework.beans.factory.InitializingBean;

import com.matrix.swan.mvc.component.ComponentConstants;


/**
 * 表示该对象可以渲染到页面。
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年10月13日 上午10:02:48
 */
@WebModuleVariable
public interface WebModule extends InitializingBean, ComponentConstants {
	
	/**
	 * 设置该Module要处理的目标资源。
	 * 
	 * @param target
	 * @return
	 */
	WebModule setTarget(Object target);
	
	/**
	 * 渲染当前的对象.
	 * <p>
	 * 建议实现的时候将该方法返回的结果返回到{@link #toString()}
	 * 
	 * @return
	 */
	String render();

	/**
	 * 渲染当前的对象.
	 * 
	 * @return
	 */
	String toString();
}