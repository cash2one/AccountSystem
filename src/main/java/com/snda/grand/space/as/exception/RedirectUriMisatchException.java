package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class RedirectUriMisatchException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6853148743098603007L;

	@Override
	public String getMessage() {
		return "Redirect uri does not match that in authorize request.";
	}

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "RedirectUriMisatch";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
