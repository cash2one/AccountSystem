package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidRequestParamsException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1339199163088931881L;
	private String param;

	public InvalidRequestParamsException(String param) {
		this.param = param;
	}

	@Override
	public String getMessage() {
		return "Invalid " + param + " param.";
	}

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "InvalidRequestParams";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
