package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class NoSuchSndaIdException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8596297600556557394L;

	public NoSuchSndaIdException() {
		super(Response
				.status(Status.NOT_FOUND)
				.entity("No such snda_id.")
				.build());
	}
	
}
