package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class InvalidAppDescriptionException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5974290649866684473L;

	public InvalidAppDescriptionException() {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("Invalid application description.")
				.build());
	}

	@Override
	public String getMessage() {
		return "Invalid application description.";
	}

}
