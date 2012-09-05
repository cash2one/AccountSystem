package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


public class AccessTokenExpiredException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2586004339720644240L;

	public AccessTokenExpiredException() {
		super(Response
				.status(Status.FORBIDDEN)
				.entity("Access token has expired.")
				.build());
	}

	@Override
	public String getMessage() {
		return "Access token has expired.";
	}

}
