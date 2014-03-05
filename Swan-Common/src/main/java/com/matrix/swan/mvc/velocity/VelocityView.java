package com.matrix.swan.mvc.velocity;

import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.util.ContextAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.velocity.VelocityConfig;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;
import org.springframework.web.util.NestedServletException;

import com.matrix.swan.mvc.common.WebModuleVariable;

/**
 * Velocity模板视图。
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年10月13日 上午10:04:19
 */
public class VelocityView extends AbstractTemplateView {
	/** 模板编码格式 */
	protected String encoding;
	/** Velocity解析引擎 */
	protected VelocityEngine velocityEngine;
	/** 模板文件的根路径目录 */
	protected String templates = null;
	/** screen模板解析的视图目录 */
	protected String screen = null;
	/** layout模板解析的视图目录 */
	protected String layout = null;

	/** velocity模板默认加载的layout模板 */
	protected String defaultLayoutTemplate = null;
	/** screen模板key */
	protected String screenTemplateKey = null;

	/** 本次请求对应的视图名称 */
	protected String viewName = null;
	/** 模板对应的扩展名称 */
	protected String suffix = null;

	@Override
	protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		exposeHelpers(model, request);
		Context velocityContext = createVelocityContext(model, request, response);
		Map<String, Object> templateVariables = getApplicationContext().getBeansWithAnnotation(WebModuleVariable.class);
		if (templateVariables != null && templateVariables.size() > 0) {
			for (Map.Entry<String, Object> e : templateVariables.entrySet()) {
				// 注入自定义工具
				String name = e.getKey();
				Object tool = e.getValue();
				if (tool == null) {
					continue;
				}
				if (tool instanceof ContextAware) {
					((ContextAware) tool).setContext(velocityContext);
				}
				velocityContext.put(name, tool);
			}
		}
		exposeHelpers(velocityContext, request, response);
		doRender(velocityContext, response);
	}

	protected void doRender(Context context, HttpServletResponse response) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Rendering Velocity template [" + getUrl() + "] in VelocityView '" + getBeanName()
					+ "', velocity '" + viewName + "'");
		}

		Template screenTemplate = getTemplate(getUrl());
		// 同名的layout
		String layoutTemplateURL = templates + layout + viewName + suffix;
		Template layoutTemplate = loadTemplate(layoutTemplateURL);

		if (layoutTemplate == null) {// 默认的layout
			layoutTemplateURL = templates + layout + defaultLayoutTemplate;
			layoutTemplate = loadTemplate(layoutTemplateURL);
		}

		if (layoutTemplate == null) {// 没有找到layout就只解析screen
			mergeTemplate(screenTemplate, context, response);
			return;
		}

		context.put(screenTemplateKey, templateRender(context, screenTemplate));
		mergeTemplate(layoutTemplate, context, response);
	}

	/**
	 * 渲染一个模板。
	 * 
	 * @param context
	 * @param template
	 * @return
	 */
	protected String templateRender(Context context, Template template) {
		if (template == null || context == null) {
			return "";
		}
		try {
			StringWriter sw = new StringWriter();
			template.merge(context, sw);
			return sw.toString();
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(String.format("Template[%s] Render Error.", template.getName()), e);
			}
		}
		return "";
	}

	/**
	 * 安全的校验并获取模板。
	 * 
	 * @param name
	 * @return
	 */
	protected Template loadTemplate(String name) {
		try {
			return getTemplate(name);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("Loading  Template: " + name + " error.", e);
			}
		}
		return null;
	}

	/**
	 * Retrieve the Velocity template specified by the given name, using the
	 * encoding specified by the "encoding" bean property.
	 * <p>
	 * Can be called by subclasses to retrieve a specific template, for example
	 * to render multiple templates into a single view.
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
	 * Merge the template with the context. Can be overridden to customize the
	 * behavior.
	 * 
	 * @param template
	 *            the template to merge
	 * @param context
	 *            the Velocity context to use for rendering
	 * @param response
	 *            servlet response (use this to get the OutputStream or Writer)
	 * @throws Exception
	 *             if thrown by Velocity
	 * @see org.apache.velocity.Template#merge
	 */
	protected void mergeTemplate(Template template, Context context, HttpServletResponse response) throws Exception {
		try {
			template.merge(context, response.getWriter());
		} catch (MethodInvocationException ex) {
			Throwable cause = ex.getWrappedThrowable();
			throw new NestedServletException("Method invocation failed during rendering of Velocity view with name '"
					+ getBeanName() + "': " + ex.getMessage() + "; reference [" + ex.getReferenceName() + "], method '"
					+ ex.getMethodName() + "'", cause == null ? ex : cause);
		}
	}

	/**
	 * Invoked on startup. Looks for a single VelocityConfig bean to find the
	 * relevant VelocityEngine for this factory.
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
	 * Autodetect a VelocityEngine via the ApplicationContext. Called if no
	 * explicit VelocityEngine has been specified.
	 * 
	 * @return the VelocityEngine to use for VelocityViews
	 * @throws BeansException
	 *             if no VelocityEngine could be found
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

	/**
	 * Expose helpers unique to each rendering operation. This is necessary so
	 * that different rendering operations can't overwrite each other's formats
	 * etc.
	 * <p>
	 * Called by {@code renderMergedTemplateModel}. The default implementation
	 * is empty. This method can be overridden to add custom helpers to the
	 * model.
	 * 
	 * @param model
	 *            the model that will be passed to the template for merging
	 * @param request
	 *            current HTTP request
	 * @throws Exception
	 *             if there's a fatal error while we're adding model attributes
	 * @see #renderMergedTemplateModel
	 */
	protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
	}

	/**
	 * Create a Velocity Context instance for the given model, to be passed to
	 * the template for merging.
	 * <p>
	 * The default implementation delegates to
	 * {@link #createVelocityContext(Map)}. Can be overridden for a special
	 * context class, for example ChainedContext which is part of the view
	 * package of Velocity Tools. ChainedContext is needed for initialization of
	 * ViewTool instances.
	 * <p>
	 * Have a look at {@link VelocityToolboxView}, which pre-implements
	 * ChainedContext support. This is not part of the standard VelocityView
	 * class in order to avoid a required dependency on the view package of
	 * Velocity Tools.
	 * 
	 * @param model
	 *            the model Map, containing the model attributes to be exposed
	 *            to the view
	 * @param request
	 *            current HTTP request
	 * @param response
	 *            current HTTP response
	 * @return the Velocity Context
	 * @throws Exception
	 *             if there's a fatal error while creating the context
	 * @see #createVelocityContext(Map)
	 * @see #initTool
	 * @see org.apache.velocity.tools.view.context.ChainedContext
	 * @see VelocityToolboxView
	 */
	protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return createVelocityContext(model);
	}

	/**
	 * Create a Velocity Context instance for the given model, to be passed to
	 * the template for merging.
	 * <p>
	 * Default implementation creates an instance of Velocity's VelocityContext
	 * implementation class.
	 * 
	 * @param model
	 *            the model Map, containing the model attributes to be exposed
	 *            to the view
	 * @return the Velocity Context
	 * @throws Exception
	 *             if there's a fatal error while creating the context
	 * @see org.apache.velocity.VelocityContext
	 */
	protected Context createVelocityContext(Map<String, Object> model) throws Exception {
		return new VelocityContext(model);
	}

	/**
	 * Expose helpers unique to each rendering operation. This is necessary so
	 * that different rendering operations can't overwrite each other's formats
	 * etc.
	 * <p>
	 * Called by {@code renderMergedTemplateModel}. Default implementation
	 * delegates to {@code exposeHelpers(velocityContext, request)}. This method
	 * can be overridden to add special tools to the context, needing the
	 * servlet response to initialize (see Velocity Tools, for example LinkTool
	 * and ViewTool/ChainedContext).
	 * 
	 * @param velocityContext
	 *            Velocity context that will be passed to the template
	 * @param request
	 *            current HTTP request
	 * @param response
	 *            current HTTP response
	 * @throws Exception
	 *             if there's a fatal error while we're adding model attributes
	 * @see #exposeHelpers(org.apache.velocity.context.Context,
	 *      HttpServletRequest)
	 */
	protected void exposeHelpers(Context velocityContext, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		exposeHelpers(velocityContext, request);
	}

	/**
	 * Expose helpers unique to each rendering operation. This is necessary so
	 * that different rendering operations can't overwrite each other's formats
	 * etc.
	 * <p>
	 * Default implementation is empty. This method can be overridden to add
	 * custom helpers to the Velocity context.
	 * 
	 * @param velocityContext
	 *            Velocity context that will be passed to the template
	 * @param request
	 *            current HTTP request
	 * @throws Exception
	 *             if there's a fatal error while we're adding model attributes
	 * @see #exposeHelpers(Map, HttpServletRequest)
	 */
	protected void exposeHelpers(Context velocityContext, HttpServletRequest request) throws Exception {
	}

	/**
	 * @param viewName
	 *            the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Set the VelocityEngine to be used by this view.
	 * <p>
	 * If this is not set, the default lookup will occur: A single
	 * VelocityConfig is expected in the current web application context, with
	 * any bean name.
	 * 
	 * @see VelocityConfig
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	/**
	 * @param suffix
	 *            the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * @param screenTemplateKey
	 *            the screenTemplateKey to set
	 */
	public void setScreenTemplateKey(String screenTemplateKey) {
		this.screenTemplateKey = screenTemplateKey;
	}

	/**
	 * @param defaultLayoutTemplate
	 *            the defaultLayoutTemplate to set
	 */
	public void setDefaultLayoutTemplate(String defaultLayoutTemplate) {
		this.defaultLayoutTemplate = defaultLayoutTemplate;
	}

	/**
	 * @param layout
	 *            the layout to set
	 */
	public void setLayout(String layout) {
		this.layout = layout;
	}

	/**
	 * @param screen
	 *            the screen to set
	 */
	public void setScreen(String screen) {
		this.screen = screen;
	}

	/**
	 * @param templates
	 *            the templates to set
	 */
	public void setTemplates(String templates) {
		this.templates = templates;
	}
}