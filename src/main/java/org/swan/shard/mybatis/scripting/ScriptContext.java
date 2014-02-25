package org.swan.shard.mybatis.scripting;

import javax.script.SimpleScriptContext;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年2月25日 下午2:08:54
 */
public class ScriptContext extends SimpleScriptContext {

	public ScriptContext put(String name, Object value) {
		setAttribute(name, value, ENGINE_SCOPE);
		return this;
	}
}