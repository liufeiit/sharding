package com.matrix.swan.mvc.velocity;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.Log4JLogChute;
import org.apache.velocity.runtime.resource.ResourceCacheImpl;
import org.apache.velocity.runtime.resource.ResourceManagerImpl;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;
import org.apache.velocity.runtime.resource.loader.JarResourceLoader;
import org.apache.velocity.runtime.resource.loader.URLResourceLoader;
import org.apache.velocity.tools.view.WebappResourceLoader;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

import com.matrix.swan.mvc.component.ComponentConstants;

/**
 * velocity模板引擎配置。
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年10月13日 上午10:04:10
 */
public class VelocityTemplateConfigurer extends VelocityConfigurer implements ComponentConstants {
	/** inputEncoding */
	public static final String DEFAULT_INPUT_ENCODING = "UTF-8";
	/** outputEncoding */
	public static final String DEFAULT_OUTPUT_ENCODING = "UTF-8";

	/** velocity模板contentType */
	protected String contentType = null;
	/** inputEncoding */
	protected String inputEncoding = null;
	/** outputEncoding */
	protected String outputEncoding = null;

	@Override
	public void afterPropertiesSet() throws IOException, VelocityException {
		setOverrideLogging(true);
		if (StringUtils.isEmpty(contentType)) {
			contentType = DEFAULT_CONTENT_TYPE;
		}
		if (StringUtils.isEmpty(inputEncoding)) {
			inputEncoding = DEFAULT_INPUT_ENCODING;
		}
		if (StringUtils.isEmpty(outputEncoding)) {
			outputEncoding = DEFAULT_OUTPUT_ENCODING;
		}
		super.afterPropertiesSet();
	}

	@Override
	protected void postProcessVelocityEngine(VelocityEngine velocityEngine) {
		super.postProcessVelocityEngine(velocityEngine);
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "webapp,file,class,url,jar,spring,springMacro");
		velocityEngine.setProperty("webapp.resource.loader.class", WebappResourceLoader.class.getName());
		velocityEngine.setProperty("file.resource.loader.class", FileResourceLoader.class.getName());
		velocityEngine.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());
		velocityEngine.setProperty("url.resource.loader.class", URLResourceLoader.class.getName());
		velocityEngine.setProperty("jar.resource.loader.class", JarResourceLoader.class.getName());

		velocityEngine.setProperty("resource.manager.cache.class", ResourceCacheImpl.class.getName());
		velocityEngine.setProperty("resource.manager.cache.size", 2048);
		velocityEngine.setProperty("resource.manager.class", ResourceManagerImpl.class.getName());

		velocityEngine.setProperty(RuntimeConstants.COUNTER_INITIAL_VALUE, 1);
		velocityEngine.setProperty(RuntimeConstants.INPUT_ENCODING, inputEncoding);
		velocityEngine.setProperty(RuntimeConstants.OUTPUT_ENCODING, outputEncoding);
		velocityEngine.setProperty("contentType", contentType);
		// org.apache.velocity.runtime.log.AvalonLogChute
		// org.apache.velocity.runtime.log.Log4JLogChute
		// org.apache.velocity.runtime.log.CommonsLogLogChute
		// org.apache.velocity.runtime.log.ServletLogChute
		// org.apache.velocity.runtime.log.JdkLogChute
		velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new Log4JLogChute());
		velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, true);
	}
}