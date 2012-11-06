package com.snda.grand.mobile.as.memcached;


/**
 * 
 * @author wangzijian
 * 
 */
public interface CacheConverter {

	String encode(Object object);

	<T> T decode(Class<T> type, String cached);
}
