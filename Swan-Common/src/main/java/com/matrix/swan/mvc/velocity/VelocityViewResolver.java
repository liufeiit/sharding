package com.matrix.swan.mvc.velocity;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import com.matrix.swan.mvc.component.ComponentConstants;

/**
 * Velocity模板视图解析器。
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年10月13日 上午10:04:29
 */
public class VelocityViewResolver extends AbstractTemplateViewResolver implements InitializingBean, ComponentConstants {
	/** 编码 */
	protected String charset = DEFAULT_CHARSET;
	/** 模板文件的根路径目录 */
	protected String templates = DEFAULT_TEMPLATES;
	/** screen模板解析的视图目录 */
	protected String screen = DEFAULT_SCREEN;
	/** layout模板解析的视图目录 */
	protected String layout = DEFAULT_LAYOUT;
	/** velocity模板默认加载的layout模板 */
	protected String defaultLayoutTemplate = DEFAULT_VELOCITY_LAYOUT_TEMPLATE;
	/** 在layout模板使用的screen模板key */
	protected String screenTemplateKey = DEFAULT_SCREEN_TEMPLATE_KEY;

	public VelocityViewResolver() {
		super();
		setViewClass(requiredViewClass());
		setExposeRequestAttributes(true);
		setAllowRequestOverride(true);
		setExposeSessionAttributes(true);
		setAllowSessionOverride(true);
		setExposeSpringMacroHelpers(true);
		setExposePathVariables(true);
		setCache(true);
		setCacheLimit(DEFAULT_CACHE_LIMIT);
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		VelocityView view = VelocityView.class.cast(super.buildView(viewName));
		view.setViewName(viewName);

		view.setContentType(getContentType());
		view.setEncoding(charset);

		view.setTemplates(templates);
		view.setScreen(screen);
		view.setLayout(layout);
		view.setSuffix(getSuffix());
		view.setScreenTemplateKey(screenTemplateKey);
		view.setDefaultLayoutTemplate(defaultLayoutTemplate);

		return view;
	}

	@Override
	protected Class<?> requiredViewClass() {
		return VelocityView.class;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (StringUtils.isEmpty(templates)) {
			templates = DEFAULT_TEMPLATES;
		}
		if (StringUtils.isEmpty(screen)) {
			screen = DEFAULT_SCREEN;
		}
		if (StringUtils.isEmpty(layout)) {
			layout = DEFAULT_LAYOUT;
		}
		if (StringUtils.isEmpty(getSuffix())) {// 默认配置
			setSuffix(DEFAULT_VELOCITY_SUFFIX);
		}
		if (StringUtils.isEmpty(charset)) {
			charset = DEFAULT_CHARSET;
		}
		if (StringUtils.isEmpty(getContentType())) {
			setContentType(MediaType.TEXT_HTML_VALUE + ";charset=" + charset);
		}
		// 设置prefix
		setPrefix(SYSTEM_SEPARATOR + templates + screen + SYSTEM_SEPARATOR);
		if (StringUtils.isEmpty(defaultLayoutTemplate)) {
			defaultLayoutTemplate = DEFAULT_VELOCITY_LAYOUT_TEMPLATE;
		}
		if (StringUtils.isEmpty(screenTemplateKey)) {
			screenTemplateKey = DEFAULT_SCREEN_TEMPLATE_KEY;
		}
	}

	/**
	 * @param charset
	 *            the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
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
	 * @param templates
	 *            the templates to set
	 */
	public void setTemplates(String templates) {
		this.templates = templates;
	}

	/**
	 * @param screen
	 *            the screen to set
	 */
	public void setScreen(String screen) {
		this.screen = screen;
	}
}