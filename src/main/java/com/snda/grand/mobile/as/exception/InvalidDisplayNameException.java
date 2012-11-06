package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidDisplayNameException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4267329429003183033L;
	
	@Override
	public String getMessage() {
		return "Invalid display_name.";
	}

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "InvalidDisplayName";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}
	
}
