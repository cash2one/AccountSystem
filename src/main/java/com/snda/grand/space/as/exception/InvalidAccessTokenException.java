package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class InvalidAccessTokenException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2598615531543275532L;

	public InvalidAccessTokenException() {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("Invalid access token.")
				.build());
	}

}
