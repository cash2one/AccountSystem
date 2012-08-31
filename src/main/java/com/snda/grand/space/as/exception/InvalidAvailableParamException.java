package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class InvalidAvailableParamException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3402086043060984469L;

	public InvalidAvailableParamException() {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("Invalid available param.")
				.build());
	}

}
