package org.swan.shard.mybatis.interceptor;

import java.util.Properties;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.swan.shard.mybatis.scripting.DefaultScriptEngine;
import org.swan.shard.mybatis.scripting.ScriptEngine;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年2月25日 下午10:51:22
 */
public abstract class BaseInterceptor implements Interceptor {
	protected final Log log = LogFactory.getLog(getClass());

	protected static final String DOT = ".";
	protected static final String PROXY_TARGET_METHOD = "target";
	protected static final String PROXY_H_METHOD = "h";

	protected final ObjectFactory of = new DefaultObjectFactory();
	protected final ObjectWrapperFactory owf = new DefaultObjectWrapperFactory();
	protected final ScriptEngine se = new DefaultScriptEngine("JavaScript");
	
	protected Properties properties;
	
	protected abstract Object interceptInternal(Invocation invocation) throws Throwable;
	
	protected abstract Object setupPlugin(Object target) throws Throwable;
	
	@Override
	public final Object intercept(Invocation invocation) throws Throwable {
		try {
			return interceptInternal(invocation);
		} catch (Throwable e) {
			log.error("Interceptor Intercept Error.", e);
		}
		return invocation.proceed();
	}

	@Override
	public final Object plugin(Object target) {
		try {
			return setupPlugin(target);
		} catch (Throwable e) {
			log.error("Setup Plugin Error.", e);
		}
		return target;
	}

	@Override
	public final void setProperties(Properties properties) {
		this.properties = properties;
		try {
			init();
		} catch (Throwable e) {
			log.error("Init Interceptor Error.", e);
		}
	}
	
	protected void init() throws Throwable {
		
	}
}