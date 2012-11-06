package com.snda.grand.mobile.as.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snda.grand.mobile.as.util.ApplicationServiceContext;
import com.snda.grand.mobile.as.util.Loggers;
import com.snda.grand.mobile.as.util.ThreadBoundServiceContext;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

public class AccountRestServlet extends SpringServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7104188436135320851L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountRestServlet.class);
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApplicationServiceContext serviceContext = new ApplicationServiceContext(request, response);
		ThreadBoundServiceContext.set(serviceContext);
		try {
			super.service(request, response);
		} finally {
			ThreadBoundServiceContext.remove();
			logAccess(serviceContext);
		}
	}
	
	private void logAccess(ApplicationServiceContext serviceContext) {
		try {
			Loggers.ACCESS_LOGGER.info(serviceContext.toString());
		} catch (Exception e) {
			LOGGER.error("Log access failed.", e);
		}
	}

}
