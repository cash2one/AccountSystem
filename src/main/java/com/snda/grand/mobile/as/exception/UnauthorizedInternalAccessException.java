package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class UnauthorizedInternalAccessException extends
		ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4459972476490335275L;
	
	@Override
	public Status getStatus() {
		return Status.UNAUTHORIZED;
	}

	@Override
	public String getCode() {
		return "UnauthorizedInternalAccess";
	}

	@Override
	public String getMessage() {
		return "Internal access is unauthorized.";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
