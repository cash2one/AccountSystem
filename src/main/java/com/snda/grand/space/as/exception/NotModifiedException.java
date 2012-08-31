package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class NotModifiedException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7675894730957864999L;

	public NotModifiedException() {
		super(Response
				.status(Status.NOT_MODIFIED)
				.entity("Not modified.")
				.build());
	}

}
