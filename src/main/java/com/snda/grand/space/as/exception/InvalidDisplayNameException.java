package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class InvalidDisplayNameException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4267329429003183033L;
	
	public InvalidDisplayNameException() {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("Invalid display_name.")
				.build());
	}
	
}
