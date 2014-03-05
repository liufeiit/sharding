package com.matrix.swan.common;

import java.io.File;
import java.io.IOException;

/**
 * 自动创建文件目录的Appender
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2013年12月30日 下午10:02:23
 */
public class DailyRollingFileAppender extends org.apache.log4j.DailyRollingFileAppender {
	public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize)
			throws IOException {
		File logfile = new File(fileName);
		logfile.getParentFile().mkdirs();
		super.setFile(fileName, append, bufferedIO, bufferSize);
	}
}