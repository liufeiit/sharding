package com.matrix.swan.mvc.component;

import java.io.File;

import org.springframework.http.MediaType;

/**
 * web mvc模块常量。
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年10月13日 上午10:03:54
 */
public interface ComponentConstants {
	String PATH_VAR = "/";
	char SYSTEM_SEPARATOR = File.separatorChar;
	/** 约定：默认配置的模板的各个模块根路径(/WEB-INF/templates/) */
	String DEFAULT_TEMPLATES = SYSTEM_SEPARATOR + "WEB-INF" + SYSTEM_SEPARATOR + "templates" + SYSTEM_SEPARATOR;
	/** 约定：默认配置的模板的screen模块路径(screen/) */
	String DEFAULT_SCREEN = "screen" + SYSTEM_SEPARATOR;
	/** 约定：默认配置的模板的layout模块路径(layout/) */
	String DEFAULT_LAYOUT = "layout" + SYSTEM_SEPARATOR;
	/** 约定：默认配置的模板的扩展名 */
	String DEFAULT_VELOCITY_SUFFIX = ".vm";
	/** 约定：默认MediaType */
	String DEFAULT_MEDIA_TYPE = MediaType.TEXT_HTML_VALUE;
	/** 约定：默认编码 */
	String DEFAULT_CHARSET = "UTF-8";
	/** 约定：模板contentType */
	String DEFAULT_CONTENT_TYPE = MediaType.TEXT_HTML_VALUE + ";charset=" + DEFAULT_CHARSET;
	/** 约定：默认的layout模板 */
	String DEFAULT_VELOCITY_LAYOUT_TEMPLATE = "default" + DEFAULT_VELOCITY_SUFFIX;
	/** 约定：默认的screen模板key */
	String DEFAULT_SCREEN_TEMPLATE_KEY = "screen_placeholder";
}