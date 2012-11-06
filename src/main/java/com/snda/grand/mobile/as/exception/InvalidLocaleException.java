package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidLocaleException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5901088204857072163L;

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "InvalidLocale";
	}

	@Override
	public String getMessage() {
		return "Invalid locale.";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
