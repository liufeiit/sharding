package com.matrix.swan.configurer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.matrix.swan.common.Server;
import com.matrix.swan.util.NumberUtil;
import com.matrix.swan.util.ResourceLoader;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年12月30日 下午4:46:14
 */
public class SystemConfigurer {
	private final static Log log = LogFactory.getLog(SystemConfigurer.class);
	private final static Properties configurer = new Properties();
	private final static String ENV_KEY = "sys.server.configurer.env";

	private static Server redisServer = null;
	private static String redisHost = null;
	private static int redisPort = -1;
	
	static {
		InputStream stream = ResourceLoader.getStream(System.getProperty(ENV_KEY, "classpath:configurer/production.properties"));
		if(stream == null) {
			log.error("Can't found SystemConfigurer Source.");
		} else {
			try {
				SystemConfigurer.configurer.load(stream);
			} catch (IOException e) {
				log.error("Can't load SystemConfigurer Source.", e);
			}
		}
	}
	
	public static Server getRedisServer() {
		if(SystemConfigurer.redisServer != null) {
			return SystemConfigurer.redisServer;
		}
		SystemConfigurer.redisServer = new Server();
		SystemConfigurer.redisServer.setHost(SystemConfigurer.getRedisHost());
		SystemConfigurer.redisServer.setPort(SystemConfigurer.getRedisPort());
		return SystemConfigurer.redisServer;
	}
	
	private static String getRedisHost() {
		if(SystemConfigurer.redisHost == null) {
			SystemConfigurer.redisHost = SystemConfigurer.configurer.getProperty("redis.host", "127.0.0.1");
		}
		return SystemConfigurer.redisHost;
	}
	
	private static int getRedisPort() {
		if(SystemConfigurer.redisPort <= 0) {
			SystemConfigurer.redisPort = NumberUtil.toInt(SystemConfigurer.configurer.getProperty("redis.port", "0"), 0);
		}
		return SystemConfigurer.redisPort;
	}
}