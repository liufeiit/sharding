package com.matrix.swan.cache;

import com.matrix.swan.common.Server;
import com.matrix.swan.configurer.SystemConfigurer;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年12月30日 下午3:35:12
 */
public class JedisPoolUtil {
	private static JedisPool pool = null;
	
	static JedisPool getJedisPool() {
		if(JedisPoolUtil.pool != null) {
			return JedisPoolUtil.pool;
		}
		JedisPoolConfig config = new JedisPoolConfig();
		// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；如果赋值为-1，则表示不限制；
		config.setMaxActive(-1);
		config.setMaxIdle(60);
		config.setMaxWait(2000);//1000l
		config.setTestOnBorrow(true);//false
		// 如果为true，表示有一个idle object
		// evitor线程对idle
		// object进行扫描，如果validate失败，此object会被从pool中drop掉；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
		config.setTestWhileIdle(true);
		// 表示一个对象至少停留在idle状态的最短时间，然后才能被idle
		// object
		// evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
		config.setMinEvictableIdleTimeMillis(60000);
		// 表示idle object
		// evitor每次扫描的最多的对象数；
		config.setNumTestsPerEvictionRun(5000);//-1
		// 表示idle object
		// evitor两次扫描之间要sleep的毫秒数；
		config.setTimeBetweenEvictionRunsMillis(5000);//30000
		Server server = SystemConfigurer.getRedisServer();
		JedisPoolUtil.pool = new JedisPool(config, server.getHost(), server.getPort(), 3000);//10000
		return JedisPoolUtil.pool;
	}
}
