package com.snda.grand.mobile.as.memcached;

import org.codehaus.jackson.map.ObjectMapper;

import com.snda.grand.mobile.as.rest.util.ObjectMappers;

/**
 * 
 * @author wangzijian
 * 
 */
public class JacksonCacheConverter implements CacheConverter {
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Override
	public String encode(Object object) {
		return ObjectMappers.toJSON(OBJECT_MAPPER, object);
	}

	@Override
	public <T> T decode(Class<T> type, String cached) {
		return ObjectMappers.readJSON(OBJECT_MAPPER, cached, type);
	}

}
