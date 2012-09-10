package com.snda.grand.space.as.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessorAuthenticateFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccessorAuthenticateFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		LOGGER.info("==========Filter==========");
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
