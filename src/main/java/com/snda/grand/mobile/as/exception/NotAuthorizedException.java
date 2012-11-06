package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class NotAuthorizedException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1515393226430293466L;

	@Override
	public String getMessage() {
		return "Not authorized.";
	}

	@Override
	public Status getStatus() {
		return Status.UNAUTHORIZED;
	}

	@Override
	public String getCode() {
		return "NotAuthorized";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
