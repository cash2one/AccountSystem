package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidAppStatusException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 897423326396684777L;

	@Override
	public String getMessage() {
		return "Invalid application status.";
	}

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "InvalidAppStatus";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
