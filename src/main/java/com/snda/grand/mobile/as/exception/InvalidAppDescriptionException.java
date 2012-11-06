package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidAppDescriptionException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5974290649866684473L;

	@Override
	public String getMessage() {
		return "Invalid application description.";
	}

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "InvalidAppDescription";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
