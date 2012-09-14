package com.snda.grand.space.as.util;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * 
 * @author wangzijian
 * 
 */
public class ApplicationServiceContext {

	private static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
	private static final Joiner MESSAGE_JOINER = Joiner
			.on(" ")
			.useForNull("-");
	
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final String id;
	private final Thread thread;
	private final DateTime creation;
	
	public ApplicationServiceContext(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.id = UUID.randomUUID().toString();
		this.thread = Thread.currentThread();
		this.creation = new DateTime();
	}

	@Override
	public String toString() {
		return join(
				datetime(),
				session(), 
				server(), 
				client(),
				path(), 
				method(), 
				status(),
				duration());
	}
	
	private String duration() {
		return String.valueOf(System.currentTimeMillis() - creation.getMillis());
	}

	private String status() {
		return String.valueOf(response.getStatus());
	}

	private String method() {
		return request.getMethod();
	}
	
	private String path() {
		return request.getPathTranslated();
	}

	private String client() {
		return request.getRemoteAddr();
	}

	private String server() {
		return request.getLocalAddr();
	}

	private String session() {
		return id;
	}

	private String datetime() {
		return new DateTime().toString(YYYY_MM_DD_HH_MM_SS_SSS);
	}
	
	private String join(String... fields) {
		return MESSAGE_JOINER.join(trimToNull(fields));
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Thread getThread() {
		return thread;
	}

	public String getId() {
		return id;
	}

	public DateTime getCreation() {
		return creation;
	}

	private static List<String> trimToNull(String... parts) {
		List<String> list = Lists.newArrayListWithCapacity(parts.length);
		for (String each : parts) {
			list.add(StringUtils.trimToNull(each));
		}
		return list;
	}
}
