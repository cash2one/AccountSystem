package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class NoSuchApplicationException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8035755614585576933L;

	@Override
	public String getMessage() {
		return "No such application.";
	}

	@Override
	public Status getStatus() {
		return Status.NOT_FOUND;
	}

	@Override
	public String getCode() {
		return "NoSuchApplication";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
