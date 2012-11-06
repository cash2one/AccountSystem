package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class ApplicationAlreadyExistException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -403211923741416314L;

	@Override
	public String getMessage() {
		return "Application already exist.";
	}

	@Override
	public Status getStatus() {
		return Status.CONFLICT;
	}

	@Override
	public String getCode() {
		return "ApplicationAlreadyExist";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
