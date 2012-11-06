package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidClientException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3210392506024722683L;

	@Override
	public Status getStatus() {
		return Status.NOT_FOUND;
	}

	@Override
	public String getCode() {
		return "invalid_client";
	}

	@Override
	public String getMessage() {
		return "Invalid client";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
