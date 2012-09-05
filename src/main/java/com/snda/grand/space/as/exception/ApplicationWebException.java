package com.snda.grand.space.as.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public abstract class ApplicationWebException extends WebApplicationException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ApplicationWebException(Response response) {
		super(response);
	}

	public Status getStatus() {
		return Status.fromStatusCode(super.getResponse().getStatus());
	}
	
	public Object getEntity() {
		return super.getResponse().getEntity();
	}
	
	public abstract String getMessage();
	
}
