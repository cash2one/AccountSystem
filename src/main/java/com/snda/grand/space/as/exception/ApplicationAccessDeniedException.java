package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ApplicationAccessDeniedException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3442442370282852466L;

	public ApplicationAccessDeniedException() {
		super(Response
				.status(Status.FORBIDDEN)
				.entity("Application access denied.")
				.build());
	}

	@Override
	public String getMessage() {
		return "Application access denied.";
	}

}
