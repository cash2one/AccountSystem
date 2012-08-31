package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ApplicationAlreadyExistException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -403211923741416314L;

	public ApplicationAlreadyExistException() {
		super(Response
				.status(Status.CONFLICT)
				.entity("Application already exist.")
				.build());
	}

}
