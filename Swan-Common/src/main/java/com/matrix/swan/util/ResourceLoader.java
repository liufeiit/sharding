package com.matrix.swan.util;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年12月30日 下午5:36:02
 */
public class ResourceLoader {
	private final static Log log = LogFactory.getLog(ResourceLoader.class);

	public static Resource getResource(String sourcePath) {
		String pathToUse = StringUtils.cleanPath(sourcePath);
		if (pathToUse == null) {
			return null;
		}
		Resource resource = null;
		try {
			if (pathToUse.startsWith(org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX)) {
				resource = new ClassRelativeResourceLoader(ResourceLoader.class).getResource(pathToUse);
			}
			if (resource == null) {
				resource = new FileSystemResourceLoader().getResource(pathToUse);
			}
		} catch (Exception e) {
			log.error("getResource error.", e);
		}
		return resource;
	}

	public static InputStream getStream(String sourcePath) {
		Resource resource = ResourceLoader.getResource(sourcePath);
		if (resource == null) {
			return null;
		}
		try {
			return resource.getInputStream();
		} catch (Exception e) {
			log.error("getStream error.", e);
		}
		return null;
	}
}