package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class AccessDeniedException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9166603576929169888L;
	
	@Override
	public String getMessage() {
		return "Access Denied.";
	}

	@Override
	public String getCode() {
		return "AccessDenied";
	}

	@Override
	public Status getStatus() {
		return Status.FORBIDDEN;
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
