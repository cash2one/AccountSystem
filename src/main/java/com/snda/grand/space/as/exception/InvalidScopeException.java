package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class InvalidScopeException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1196727915670684966L;

	public InvalidScopeException() {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("Invalid scope.")
				.build());
	}

}
