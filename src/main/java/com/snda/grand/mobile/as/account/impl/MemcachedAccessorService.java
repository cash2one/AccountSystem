package com.snda.grand.mobile.as.account.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Charsets;
import com.snda.grand.mobile.as.account.AccessorService;
import com.snda.grand.mobile.as.memcached.Memcached;
import com.snda.grand.mobile.as.mongo.internal.model.Accessor;
import com.snda.grand.mobile.as.util.Loggers;

public class MemcachedAccessorService implements AccessorService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MemcachedAccessorService.class);
	
	private final AccessorService accessorService;
	private final Memcached memcached;
	
	private int expiration;
	
	public MemcachedAccessorService(AccessorService accessorService,
			Memcached memcached) {
		this.accessorService = accessorService;
		this.memcached = memcached;
		LOGGER.info("MemcachedAccessorService initialized.");
	}

	@Override
	public Accessor getAccessor(String accessKey, String secretKey) {
		Accessor cached = getCache(accessKey);
		if (cached != null) {
			LOGGER.info("Cache hit accessor access key : {}.", accessKey);
			return cached;
		}
		Accessor accessor = accessorService.getAccessor(accessKey, secretKey);
		if (accessor != null) {
			setCache(accessor);
		}
		return accessor;
	}
	
	private void setCache(Accessor accessor) {
		String memcachedId = memcachedId(accessor.getAccessKey());
		try {
			memcached.set(memcachedId, accessor, expiration);
		} catch (Exception e) {
			warn("Set", memcachedId, e);
		}
	}
	
	private Accessor getCache(String accessKey) {
		String memcachedId = memcachedId(accessKey);
		try {
			return memcached.get(Accessor.class, memcachedId);
		} catch (Exception e) {
			warn("Get", memcachedId, e);
			return null;
		}
	}
	
	private String memcachedId(String accessKey) {
		try {
			return "Accessor#" + URLEncoder.encode(accessKey, Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private void warn(String operation, String memcachedId, Throwable e) {
		Loggers.WARNING_LOGGER.error(MessageFormat.format("AccessorCache: {0} cache {1} failed, cause: {2}.",
				operation, 
				memcachedId, 
				e.toString()), 
				e);
	}
	
	@Required
	public void setExpiration(int expiration) {
		this.expiration = expiration;
	}

}
