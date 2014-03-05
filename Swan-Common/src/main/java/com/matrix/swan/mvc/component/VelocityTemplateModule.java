package com.matrix.swan.mvc.component;

import java.io.StringWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.util.ContextAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.view.velocity.VelocityConfig;

/**
 * 自定义扩展的模板Module
 * <p>
 * 用于直接解析一个模板。
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年10月13日 上午10:03:32
 */
public class VelocityTemplateModule extends BaseWebModule implements ContextAware {
	/** 所有模板文件的根路径 */
	protected String templates = null;
	/** Module模板文件的根路径 */
	protected String module = null;
	/** 加载Module模板文件的编码格式 */
	protected String encoding = null;
	/** 模板引擎 */
	protected VelocityEngine velocityEngine = null;
	/** 当前上下文 */
	protected Context context = null;
	/** 模板名称，模板的绝对路径：templates +　Module + name*/
	protected String target = null;

	@Override
	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	protected void init() throws Exception {
		if (StringUtils.isEmpty(templates)) {
			templates = DEFAULT_TEMPLATES;
		}
		if (StringUtils.isEmpty(encoding)) {
			encoding = DEFAULT_CHARSET;
		}
		module = StringUtils.defaultIfBlank(module, StringUtils.EMPTY);
	}

	@Override
	public String render() {
		if(target == null || target.isEmpty()) {
			return "";
		}
		try {
			Template controlTemplate = getTemplate(target);
			if (controlTemplate == null) {
				return "";
			}
			StringWriter sw = new StringWriter();
			controlTemplate.merge(context, sw);
			return sw.toString();
		} catch (Exception e) {
			logger.error("render template named: " + target, e);
			return "";
		}
	}

	@Override
	public VelocityTemplateModule setTarget(Object target) {
		this.target = templates + module + String.valueOf(target);
		if (logger.isDebugEnabled()) {
			logger.debug("setting Module [" + this.target + "]");
		}
		return this;
	}

	public VelocityTemplateModule setQueryData(String key, Object value) {
		if (context == null) {
			context = new VelocityContext();
		}
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("setting Module [%s] QueryData [%s=%s]", target, key, value));
		}
		context.put(key, value);
		return this;
	}

	/**
	 * Retrieve the Velocity template specified by the given name, using the
	 * encoding specified by the "encoding" bean property.
	 * 
	 * @param name
	 *            the file name of the desired template
	 * @return the Velocity template
	 * @throws Exception
	 *             if thrown by Velocity
	 * @see org.apache.velocity.app.VelocityEngine#getTemplate
	 */
	protected Template getTemplate(String name) throws Exception {
		return (encoding != null ? velocityEngine.getTemplate(name, encoding) : velocityEngine.getTemplate(name));
	}
	
	/**
 	 * Invoked on startup. Looks for a single VelocityConfig bean to
 	 * find the relevant VelocityEngine for this factory.
 	 */
	@Override
	protected void initApplicationContext() throws BeansException {
		super.initApplicationContext();
		if (velocityEngine == null) {
			// No explicit VelocityEngine: try to autodetect one.
			setVelocityEngine(autodetectVelocityEngine());
		}
	}

	/**
	 * Autodetect a VelocityEngine via the ApplicationContext.
	 * Called if no explicit VelocityEngine has been specified.
	 * @return the VelocityEngine to use for VelocityViews
	 * @throws BeansException if no VelocityEngine could be found
	 * @see #getApplicationContext
	 * @see #setVelocityEngine
	 */
	protected VelocityEngine autodetectVelocityEngine() throws BeansException {
		try {
			VelocityConfig velocityConfig = BeanFactoryUtils.beanOfTypeIncludingAncestors(getApplicationContext(),
					VelocityConfig.class, true, false);
			return velocityConfig.getVelocityEngine();
		} catch (NoSuchBeanDefinitionException ex) {
			throw new ApplicationContextException(
					"Must define a single VelocityConfig bean in this web application context "
							+ "(may be inherited): VelocityConfigurer is the usual implementation. "
							+ "This bean may be given any name.", ex);
		}
	}

	@Override
	protected boolean isContextRequired() {
		return true;
	}

	@Override
	public String toString() {
		return render();
	}
	
	/**
	 * @param templates the templates to set
	 */
	public void setTemplates(String templates) {
		this.templates = templates;
	}
	
	/**
	 * @param module the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @param velocityEngine
	 *            the velocityEngine to set
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
}