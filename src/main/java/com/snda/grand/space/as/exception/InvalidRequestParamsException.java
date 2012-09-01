package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class InvalidRequestParamsException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1339199163088931881L;

	public InvalidRequestParamsException(String param) {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("Invalid " + param + " param.")
				.build());
	}

}
