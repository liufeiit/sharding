package org.swan.shard.mybatis.interceptor;

import java.sql.Connection;

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
	private static final String DELEGATE_BOUND_SQL_SQL = "delegate.boundSql.sql";
	private static final String DELEGATE_BOUND_SQL = "delegate.boundSql";
	private static final String DELEGATE_MAPPED_STATEMENT = "delegate.mappedStatement";

	protected Object interceptInternal(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();
		if (!StatementHandler.class.isInstance(target)) {
			return invocation.proceed();
		}
		MetaObject metaStatementHandler = MetaObject.forObject(target, of, owf);
		while (metaStatementHandler.hasGetter(PROXY_H_METHOD)) {
			Object object = metaStatementHandler.getValue(PROXY_H_METHOD);
			metaStatementHandler = MetaObject.forObject(object, of, owf);
		}
		while (metaStatementHandler.hasGetter(PROXY_TARGET_METHOD)) {
			Object object = metaStatementHandler.getValue(PROXY_TARGET_METHOD);
			metaStatementHandler = MetaObject.forObject(object, of, owf);
		}
		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue(DELEGATE_MAPPED_STATEMENT);
		String mappedId = mappedStatement.getId();
		String className = mappedId.substring(0, mappedId.lastIndexOf(DOT));
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
		MetaShard metaShard = clazz.getAnnotation(MetaShard.class);
		if (metaShard == null) {
			return invocation.proceed();
		}
		BoundSql boundSql = (BoundSql) metaStatementHandler.getValue(DELEGATE_BOUND_SQL);
		MetaObject metaParameterObject = MetaObject.forObject(boundSql.getParameterObject(), of, owf);
		String[] getterNames = metaParameterObject.getGetterNames();
		ScriptContext context = new ScriptContext();
		if (getterNames != null && getterNames.length > 0) {
			for (String getterName : getterNames) {
				context.put(getterName, metaParameterObject.getValue(getterName));
			}
		}
		String sql = boundSql.getSql();
		String tableName = se.eval(metaShard.expression(), context);
		String shardingsql = StringUtils.replace(sql, metaShard.name(), tableName);
		if(log.isDebugEnabled()) {
			log.debug("ShardingSQL : " + shardingsql);
		}
		metaStatementHandler.setValue(DELEGATE_BOUND_SQL_SQL, shardingsql);
		return invocation.proceed();
	}

	@Override
	public Object setupPlugin(Object target) throws Throwable {
		if (!StatementHandler.class.isInstance(target)) {
			return target;
		}
		return Plugin.wrap(target, this);
	}
}