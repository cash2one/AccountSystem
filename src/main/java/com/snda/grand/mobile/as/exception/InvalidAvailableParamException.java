package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidAvailableParamException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3402086043060984469L;

	@Override
	public String getMessage() {
		return "Invalid available param.";
	}

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "InvalidAvailableParam";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
