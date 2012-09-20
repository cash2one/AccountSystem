package com.snda.grand.space.as.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.net.HttpHeaders;
import com.snda.grand.space.as.exception.InvalidRemoteClientException;
import com.snda.grand.space.as.util.InternalIpAllow;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

@Component
public class RemoteIpFilter implements ContainerRequestFilter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteIpFilter.class);
	private static final String IGNORE_OAUTH_REQUEST_PATH = "api/oauth2";
	
	@Context
	private HttpServletRequest httpServletRequest;
	
	private static InternalIpAllow internalIpAllow;
	
	public RemoteIpFilter(InternalIpAllow internalIpAllow) {
		RemoteIpFilter.internalIpAllow = internalIpAllow;
	}

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		if (request.getRequestUri().toString().startsWith(request.getBaseUri() + IGNORE_OAUTH_REQUEST_PATH)) {
			return request;
		}
		if (httpServletRequest != null) {
			String remoteHost = httpServletRequest
					.getHeader(HttpHeaders.X_FORWARDED_FOR) == null ? httpServletRequest
					.getRemoteHost() : httpServletRequest
					.getHeader(HttpHeaders.X_FORWARDED_FOR);
			if (internalIpAllow.contains(remoteHost)) {
				return request;
			}
		}
		throw new InvalidRemoteClientException();
	}

}
