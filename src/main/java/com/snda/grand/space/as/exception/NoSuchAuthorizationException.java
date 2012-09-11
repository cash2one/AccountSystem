package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class NoSuchAuthorizationException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 323328520871050496L;

	@Override
	public Status getStatus() {
		return Status.NOT_FOUND;
	}

	@Override
	public String getCode() {
		return "NoSuchAuthorization";
	}

	@Override
	public String getMessage() {
		return "No such authorization.";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
