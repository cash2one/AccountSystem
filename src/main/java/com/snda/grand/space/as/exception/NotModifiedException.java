package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class NotModifiedException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7675894730957864999L;

	@Override
	public String getMessage() {
		return "Not modified.";
	}

	@Override
	public Status getStatus() {
		return Status.NOT_MODIFIED;
	}

	@Override
	public String getCode() {
		return "NotModified";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
