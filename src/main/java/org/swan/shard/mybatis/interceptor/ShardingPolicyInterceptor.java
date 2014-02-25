package org.swan.shard.mybatis.interceptor;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.swan.shard.mybatis.annotation.MetaShard;
import org.swan.shard.mybatis.scripting.DefaultScriptEngine;
import org.swan.shard.mybatis.scripting.ScriptContext;
import org.swan.shard.mybatis.scripting.ScriptEngine;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年2月24日 下午5:46:22
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class ShardingPolicyInterceptor implements Interceptor {
	private static final Log log = LogFactory.getLog(ShardingPolicyInterceptor.class);

	private static final String TABLE_NAME = "table.name";
	private static final String PROXY_TARGET = "target";
	private static final String PROXY_H = "h";

	private static final ObjectFactory OF = new DefaultObjectFactory();
	private static final ObjectWrapperFactory OWF = new DefaultObjectWrapperFactory();
	private static final ScriptEngine SE = new DefaultScriptEngine("JavaScript");

	public Object intercept(Invocation invocation) throws Throwable {
		try {
			return intercept0(invocation);
		} catch (Throwable e) {
			log.error("Sharding Policy Intercept Error.", e);
		}
		return invocation.proceed();
	}
	
	public Object intercept0(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();
		if (!StatementHandler.class.isInstance(target)) {
			return invocation.proceed();
		}
		MetaObject metaStatementHandler = MetaObject.forObject(target, OF, OWF);
		while (metaStatementHandler.hasGetter(PROXY_H)) {
			Object object = metaStatementHandler.getValue(PROXY_H);
			metaStatementHandler = MetaObject.forObject(object, OF, OWF);
		}
		while (metaStatementHandler.hasGetter(PROXY_TARGET)) {
			Object object = metaStatementHandler.getValue(PROXY_TARGET);
			metaStatementHandler = MetaObject.forObject(object, OF, OWF);
		}
		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
		String id = mappedStatement.getId();
		String className = id.substring(0, id.lastIndexOf("."));
		Class<?> clazz = Class.forName(className);
		MetaShard metaShard = clazz.getAnnotation(MetaShard.class);
		if (metaShard == null) {
			return invocation.proceed();
		}
		BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
		String sql = boundSql.getSql();
		MetaObject metap = MetaObject.forObject(boundSql.getParameterObject(), OF, OWF);
		String[] getter = metap.getGetterNames();
		ScriptContext context = new ScriptContext();
		if (getter != null && getter.length > 0) {
			for (String name : getter) {
				context.put(name, metap.getValue(name));
			}
		}
		String tableName = metaShard.name() + "_" + SE.eval(metaShard.expression(), context);
		String shardingsql = sql.replaceAll(":" + properties.getProperty(TABLE_NAME), tableName);
		log.debug("ShardingSQL : " + shardingsql);
		metaStatementHandler.setValue("delegate.boundSql.sql", shardingsql);
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	private Properties properties;

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}