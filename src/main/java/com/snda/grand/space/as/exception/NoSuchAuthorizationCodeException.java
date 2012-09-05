package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class NoSuchAuthorizationCodeException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1303999864644269254L;

	public NoSuchAuthorizationCodeException() {
		super(Response
				.status(Status.NOT_FOUND)
				.entity("No such authorization code.")
				.build());
	}

}
