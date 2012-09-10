package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidEmailException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7313799623414316556L;

	@Override
	public String getMessage() {
		return "Invalid email.";
	}

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "InvalidEmail";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
