package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidAccessTokenException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2598615531543275532L;

	@Override
	public String getMessage() {
		return "Invalid access token.";
	}

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "InvalidAccessToken";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
