package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


public class InvalidSndaIdException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4769529418394565568L;
	
	public InvalidSndaIdException() {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("Invalid sndaId.")
				.build());
	}

}
