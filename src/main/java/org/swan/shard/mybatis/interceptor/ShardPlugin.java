package org.swan.shard.mybatis.interceptor;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.StringUtils;
import org.swan.shard.mybatis.annotation.MetaShard;
import org.swan.shard.mybatis.scripting.ScriptContext;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年2月24日 下午5:46:22
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class ShardPlugin extends BaseInterceptor {
	private Properties properties;
	
	protected Object interceptInternal(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();
		if (!StatementHandler.class.isInstance(target)) {
			return invocation.proceed();
		}
		MetaObject metaStatementHandler = MetaObject.forObject(target, object_factory, object_wrapper_factory);
		while (metaStatementHandler.hasGetter(proxy_h_method)) {
			Object object = metaStatementHandler.getValue(proxy_h_method);
			metaStatementHandler = MetaObject.forObject(object, object_factory, object_wrapper_factory);
		}
		while (metaStatementHandler.hasGetter(proxy_target_method)) {
			Object object = metaStatementHandler.getValue(proxy_target_method);
			metaStatementHandler = MetaObject.forObject(object, object_factory, object_wrapper_factory);
		}
		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
		String id = mappedStatement.getId();
		String className = id.substring(0, id.lastIndexOf(dot));
		Class<?> clazz = Class.forName(className);
		MetaShard metaShard = clazz.getAnnotation(MetaShard.class);
		if (metaShard == null) {
			return invocation.proceed();
		}
		BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
		String sql = boundSql.getSql();
		MetaObject metap = MetaObject.forObject(boundSql.getParameterObject(), object_factory, object_wrapper_factory);
		String[] getter = metap.getGetterNames();
		ScriptContext context = new ScriptContext();
		if (getter != null && getter.length > 0) {
			for (String name : getter) {
				context.put(name, metap.getValue(name));
			}
		}
		String tableName = metaShard.name() + "_" + script_engine.eval(metaShard.expression(), context);
		String shardingsql = StringUtils.replace(sql, ":" + properties.getProperty(table_name_variable), tableName);
		log.debug("ShardingSQL : " + shardingsql);
		metaStatementHandler.setValue("delegate.boundSql.sql", shardingsql);
		return invocation.proceed();
	}

	@Override
	public Object setupPlugin(Object target) throws Throwable {
		if (!StatementHandler.class.isInstance(target)) {
			return target;
		}
		return Plugin.wrap(target, this);
	}

	@Override
	public void init(Properties properties) throws Throwable {
		this.properties = properties;
	}
}