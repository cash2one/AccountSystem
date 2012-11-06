package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;


public class AccessTokenExpiredException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2586004339720644240L;

	@Override
	public String getMessage() {
		return "Expired token";
	}

	@Override
	public Status getStatus() {
		return Status.FORBIDDEN;
	}

	@Override
	public String getCode() {
		return "expired_token";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
