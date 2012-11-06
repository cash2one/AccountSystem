package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidGrantException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8606999574922817572L;

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "invalid_grant";
	}

	@Override
	public String getMessage() {
		return "Invalid grant";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
