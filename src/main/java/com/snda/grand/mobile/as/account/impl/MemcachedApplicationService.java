package com.snda.grand.mobile.as.account.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Charsets;
import com.snda.grand.mobile.as.account.ApplicationService;
import com.snda.grand.mobile.as.memcached.Memcached;
import com.snda.grand.mobile.as.rest.model.Application;
import com.snda.grand.mobile.as.util.Loggers;

public class MemcachedApplicationService implements ApplicationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MemcachedApplicationService.class);
	
	private final ApplicationService applicationService;
	private final Memcached memcached;
	
	private int expiration;
	
	public MemcachedApplicationService(ApplicationService applicationService,
			Memcached memcached) {
		this.applicationService = applicationService;
		this.memcached = memcached;
		LOGGER.info("MemcachedApplicationService initialized.");
	}

	@Override
	public Application putApplication(String appId, String uid, String appKey, 
			String appSecret, String appDescription,
			String appStatus, String publisherName, String scope,
			String website, long creationTime, long modifiedTime) {
		Application application = applicationService.putApplication(appId, uid,
				appKey, appSecret, appDescription, appStatus, publisherName,
				scope, website, creationTime, modifiedTime);
		setCache(application);
		LOGGER.info("Set application cache appId : {}", appId);
		return application;
	}
	
	@Override
	public Application updateApplication(String appId, String modifiedAppId, 
			String uid, String appKey, String appSecret, String appDescription,
			String appStatus, String publisherName, String scope,
			String website, long creationTime, long modifiedTime) {
		deleteCache(appId);
		Application application = applicationService.updateApplication(appId,
				modifiedAppId, uid, appKey, appSecret, appDescription,
				appStatus, publisherName, scope, website, creationTime,
				modifiedTime);
		setCache(application);
		LOGGER.info("Set application cache appId : {}", modifiedAppId);
		return application;
	}

	@Override
	public Application getApplicationByAppId(String appId) {
		Application cached = getCache(appId);
		if (cached != null) {
			LOGGER.info("Cache hit application appId : {}", appId);
			return cached;
		}
		Application application = applicationService.getApplicationByAppId(appId);
		if (application != null) {
			setCache(application);
		}
		return application;
	}

	@Override
	public void deleteApplication(String appId) {
		deleteCache(appId);
		LOGGER.info("Delete cache application appId : {}", appId);
		applicationService.deleteApplication(appId);
	}
	
	private Application getCache(String appId) {
		String memcachedId = memcachedId(appId);
		try {
			return memcached.get(Application.class, memcachedId);
		} catch (Exception e) {
			warn("Get", memcachedId, e);
			return null;
		}
	}
	
	private void setCache(Application application) {
		String memcachedId = memcachedId(application.getAppid());
		try {
			memcached.set(memcachedId, application, expiration);
		} catch (Exception e) {
			warn("Set", memcachedId, e);
		}
	}
	
	private void deleteCache(String appId) {
		String memcachedId = memcachedId(appId);
		try {
			memcached.delete(memcachedId);
		} catch (Exception e) {
			warn("Delete", memcachedId, e);
		}
	}

	private String memcachedId(String appId) {
		try {
			return "Application#" + URLEncoder.encode(appId, Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private void warn(String operation, String memcachedId, Throwable e) {
		Loggers.WARNING_LOGGER.error(MessageFormat.format("ApplicationCache: {0} cache {1} failed, cause: {2}.",
				operation, 
				memcachedId, 
				e.toString()), 
				e);
	}
	
	@Required
	public void setExpiration(int expiration) {
		this.expiration = expiration;
	}

	@Override
	public List<Application> listApplications(String owner) {
		return applicationService.listApplications(owner);
	}

}
