package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class DomainMismatchException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6410243236510085044L;

	@Override
	public String getMessage() {
		return "The domain of return url does not match the application's website property.";
	}

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "DomainMismatch";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
