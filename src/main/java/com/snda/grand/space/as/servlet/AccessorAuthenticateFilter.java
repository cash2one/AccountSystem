package com.snda.grand.space.as.servlet;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.codec.binary.Base64;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.snda.grand.space.as.exception.InvalidAccessorException;
import com.snda.grand.space.as.exception.UnauthorizedInternalAccessException;
import com.snda.grand.space.as.rest.util.Preconditions;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

@Component
public class AccessorAuthenticateFilter implements ContainerRequestFilter {
	
	private static MongoOperations mongoOps;
	private static final String IGNORE_OAUTH_REQUEST_PATH = "api/oauth2";
//	private static final String CONTAIN_OAUTH_VALIDATE_PATH = "api/oauth2/validate";
	
	public AccessorAuthenticateFilter(MongoOperations mongoOperations) {
		AccessorAuthenticateFilter.mongoOps = mongoOperations;
	}
	
	@Override
	public ContainerRequest filter(ContainerRequest request) {
		if (request.getRequestUri().toString().contains(IGNORE_OAUTH_REQUEST_PATH)
				/*&& !request.getRequestUri().toString().contains(CONTAIN_OAUTH_VALIDATE_PATH)*/) {
			return request;
		}
		String credential = getCredential(request);
		String[] pair = credential.split(":");
		if (pair.length != 2) {
			throw new InvalidAccessorException();
		}
		String accessKey = pair[0];
		String secretKey = pair[1];
		if (Preconditions.getAccessor(mongoOps, accessKey, secretKey) == null) {
			throw new InvalidAccessorException();
		}
		return request;
	}
	
	private String getCredential(ContainerRequest request) {
		String authorization = request.getHeaderValue(HttpHeaders.AUTHORIZATION);
		return getCredential(authorization);
	}
	
	public static String getCredential(String authorization) {
		if (authorization == null || !authorization.startsWith("Basic ")) {
			throw new UnauthorizedInternalAccessException();
		}
		try {
			byte[] encodedCredential = authorization.substring(6).getBytes(Charsets.UTF_8);
			byte[] decodedCredential = Base64.decodeBase64(encodedCredential);
			return new String(decodedCredential, Charsets.UTF_8);
		} catch (Exception e) {
			throw new UnauthorizedInternalAccessException();
		}
	}

}
