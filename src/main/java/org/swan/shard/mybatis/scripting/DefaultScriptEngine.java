package org.swan.shard.mybatis.scripting;

import javax.script.ScriptEngineManager;

import org.springframework.util.NumberUtils;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年2月25日 下午2:19:23
 */
public class DefaultScriptEngine implements ScriptEngine {
	private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
	private final javax.script.ScriptEngine engine;

	public DefaultScriptEngine(String name) {
		super();
		engine = SCRIPT_ENGINE_MANAGER.getEngineByName(name);
	}

	@Override
	public String eval(String script, ScriptContext context) throws ScriptException {
		try {
			Object val = engine.eval(script, context);
			if(Number.class.isInstance(val)) {
				return String.valueOf(NumberUtils.convertNumberToTargetClass((Number) val, Long.class));
			}
			return String.valueOf(val);
		} catch (javax.script.ScriptException e) {
			throw new ScriptException(e);
		}
	}
}