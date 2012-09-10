package com.snda.grand.space.as.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.google.common.net.HttpHeaders;

public class UnauthorizedInternalAccessException extends
		WebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4459972476490335275L;
	
	public UnauthorizedInternalAccessException() {
		super(unauthorizedResponse());
	}

	public UnauthorizedInternalAccessException(Throwable cause) {
		super(cause, unauthorizedResponse());
	}
	
	private static Response unauthorizedResponse() {
		return Response.status(Response.Status.UNAUTHORIZED)
				.header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Secure Area\"")
				.build();
	}

}
