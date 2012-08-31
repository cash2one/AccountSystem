package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class InvalidEmailException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7313799623414316556L;

	public InvalidEmailException() {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("Invalid email.")
				.build());
	}

}
