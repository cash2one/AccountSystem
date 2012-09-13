package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidUsernameNormException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6738163096303585396L;

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "InvalidUsernameNorm";
	}

	@Override
	public String getMessage() {
		return "Invalid username_norm";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
