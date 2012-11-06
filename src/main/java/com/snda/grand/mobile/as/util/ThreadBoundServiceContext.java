package com.snda.grand.mobile.as.util;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * 
 * @author wangzijian
 * 
 */
public class ThreadBoundServiceContext {

	private static final ThreadLocal<ApplicationServiceContext> CURRENT = new ThreadLocal<ApplicationServiceContext>();
	private static final Map<String, ApplicationServiceContext> ALL = Maps.newConcurrentMap();
	
	private ThreadBoundServiceContext() {
	}
	
	public static boolean exists() {
		return CURRENT.get() != null;
	}
	
	public static ApplicationServiceContext get() {
		return checkNotNull(CURRENT.get(), "No thread-bound ServiceContext found");
	}
	
	public static Map<String, ApplicationServiceContext> getAll() {
		return ImmutableMap.copyOf(ALL);
	}
	
	public static void set(ApplicationServiceContext serviceContext) {
		checkNotNull(serviceContext);
		CURRENT.set(serviceContext);
		ALL.put(serviceContext.getId(), serviceContext);
	}
	
	public static void remove() {
		ApplicationServiceContext removal = get();
		CURRENT.remove();
		ALL.remove(removal.getId());
	}
}
