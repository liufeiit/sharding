package com.matrix.swan.mvc;

import javax.servlet.ServletException;

import org.springframework.web.servlet.DispatcherServlet;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0
 * @since 2014年1月3日 下午10:31:27
 */
public class WebDispatcher extends DispatcherServlet {

	public final static String DEV_MODE = "devMode";
	
	private static final long serialVersionUID = 1L;
	private boolean devMode = false;

	@Override
	protected void initFrameworkServlet() throws ServletException {
		super.initFrameworkServlet();
		getServletContext().setAttribute(DEV_MODE, devMode);
		if(devMode) {
			logger.error("devMode : " + devMode);
		}
	}
	
	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}
}