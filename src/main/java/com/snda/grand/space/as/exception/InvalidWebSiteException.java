package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidWebSiteException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6619065337695289564L;

	@Override
	public String getMessage() {
		return "Invalid website.";
	}

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "InvalidWebSite";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
