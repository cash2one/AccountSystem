package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class NoSuchApplicationException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8035755614585576933L;

	public NoSuchApplicationException() {
		super(Response
				.status(Status.NOT_FOUND)
				.entity("No such application.")
				.build());
	}

}
