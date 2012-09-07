package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class DomainDoesNotMatchException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6410243236510085044L;

	public DomainDoesNotMatchException() {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("The domain of return url does not match the application's website property.")
				.build());
	}

	@Override
	public String getMessage() {
		return "The domain of return url does not match the application's website property.";
	}

}
