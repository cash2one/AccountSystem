package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class InvalidUidException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 771325027886979167L;

	public InvalidUidException() {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("Invalid uid.")
				.build());
	}

}
