package com.snda.grand.space.as.memcached;


/**
 * 
 * @author wangzijian
 * 
 */
public interface Memcached {

	<T> T get(Class<T> type, String memcachedId);

	void set(String memcachedId, Object object, int expiration);

	void delete(String memcachedId);

}
