package com.snda.grand.mobile.as.util;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 
 * @author wangzijian
 * 
 */
public class RequestIdConverter extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		String requestId = getRequestId();
		return requestId == null ? Loggers.DASH : requestId;
	}

	private String getRequestId() {
		if (ThreadBoundServiceContext.exists()) {
			return ThreadBoundServiceContext.get().getId();
		}
		return null;
	}

}
