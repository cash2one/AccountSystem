package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidAccessorException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3390390318647532015L;

	@Override
	public Status getStatus() {
		return Status.FORBIDDEN;
	}

	@Override
	public String getCode() {
		return "InvalidAccessor";
	}

	@Override
	public String getMessage() {
		return "Invalid Accessor.";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
