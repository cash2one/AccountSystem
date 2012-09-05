package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class InvalidAppStatusException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 897423326396684777L;

	public InvalidAppStatusException() {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("Invalid application status.")
				.build());
	}

	@Override
	public String getMessage() {
		return "Invalid application status.";
	}

}
