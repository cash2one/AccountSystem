package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidScopeException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1196727915670684966L;

	@Override
	public String getMessage() {
		return "Invalid scope.";
	}

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "InvalidScope";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
