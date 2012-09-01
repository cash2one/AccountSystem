package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class InvalidWebSiteException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6619065337695289564L;

	public InvalidWebSiteException() {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("Invalid website.")
				.build());
	}

}
