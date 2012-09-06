package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class RedirectUriDoesNotMatchException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6853148743098603007L;

	public RedirectUriDoesNotMatchException() {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("Redirect uri does not match that in authorize request.")
				.build());
	}

	@Override
	public String getMessage() {
		return "Redirect uri does not match that in authorize request.";
	}

}
