package org.swan.shard.mybatis.scripting;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年2月25日 下午2:07:13
 */
public interface ScriptEngine {
	String eval(String script, ScriptContext context) throws ScriptException;
}