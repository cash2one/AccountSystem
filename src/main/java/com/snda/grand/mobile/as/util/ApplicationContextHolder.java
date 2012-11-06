package com.snda.grand.mobile.as.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextHolder implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ApplicationContextHolder.applicationContext = applicationContext;
	}

	public static ApplicationContext get() {
		return applicationContext;
	}

}
