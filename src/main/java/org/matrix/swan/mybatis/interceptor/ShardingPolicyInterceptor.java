package org.matrix.swan.mybatis.interceptor;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
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
import org.matrix.swan.mybatis.type.ShardingType;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年2月24日 下午5:46:22
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class ShardingPolicyInterceptor implements Interceptor {
	private static final ObjectFactory OF = new DefaultObjectFactory();
	private static final ObjectWrapperFactory OWF = new DefaultObjectWrapperFactory();

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, OF, OWF);
		String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
		BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
		System.out.println("originalSql : " + originalSql);
		System.out.println("boundSql : " + boundSql.getSql());
		for (ParameterMapping pm : boundSql.getParameterMappings()) {
			System.out.println("Expression : " + pm.getExpression());
			System.out.println("Property : " + pm.getProperty());
			System.out.println("ResultMapId : " + pm.getResultMapId());
			System.out.println("ResultMapId : " + pm.getMode());
		}
		// Configuration configuration = (Configuration)
		// metaStatementHandler.getValue("delegate.configuration");
		Object parameterObject = metaStatementHandler.getValue("delegate.boundSql.parameterObject");
		ParamMap<?> map = (ParamMap<?>) parameterObject;
		System.out.println("nick : " + map.get("nick"));
		if (originalSql != null && !originalSql.equals("")) {
			MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
					.getValue("delegate.mappedStatement");
			String id = mappedStatement.getId();
			System.out.println("delegate.boundSql : " + mappedStatement.getBoundSql("delegate.boundSql").getSql());
			
			System.out.println("id : " + id);
			String className = id.substring(0, id.lastIndexOf("."));
			Class<?> classObj = Class.forName(className);
			// 根据配置自动生成分表SQL
			ShardingType tableSeg = classObj.getAnnotation(ShardingType.class);
			if (tableSeg != null) {
//				AnalyzeActualSql as = new AnalyzeActualSqlImpl(mappedStatement, parameterObject, boundSql);
//				String shardSql = as.getActualSql(originalSql, tableSeg);
//				if (shardSql != null) {
//					metaStatementHandler.setValue("delegate.boundSql.sql", shardSql);
//				}
			}
		}
		// 传递给下一个拦截器处理
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	@Override
	public void setProperties(Properties properties) {

	}
}