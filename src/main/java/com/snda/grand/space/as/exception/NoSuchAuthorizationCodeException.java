package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class NoSuchAuthorizationCodeException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1303999864644269254L;

	@Override
	public String getMessage() {
		return "No such authorization code.";
	}

	@Override
	public Status getStatus() {
		return Status.NOT_FOUND;
	}

	@Override
	public String getCode() {
		return "NoSuchAuthorizationCode";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
