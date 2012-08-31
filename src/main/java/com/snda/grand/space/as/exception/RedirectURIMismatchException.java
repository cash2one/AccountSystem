package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class RedirectURIMismatchException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2166047347242451143L;

	public RedirectURIMismatchException() {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("Redirect URI mismatch.")
				.build());
	}

}
