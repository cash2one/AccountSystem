package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class AccessDeniedException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9166603576929169888L;

	public AccessDeniedException() {
		super(Response
				.status(Status.FORBIDDEN)
				.entity("Access Denied.")
				.build());
	}

	@Override
	public String getMessage() {
		return "Access Denied.";
	}

}
