package com.snda.grand.space.as.exception;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class NotAuthorizedException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1515393226430293466L;

	public NotAuthorizedException() {
		super(Response
				.status(Status.UNAUTHORIZED)
				.header(HttpHeaders.WWW_AUTHENTICATE, "insert realm")
				.entity("Not authorized.")
				.build());
	}

	@Override
	public String getMessage() {
		return "Not authorized.";
	}

}
