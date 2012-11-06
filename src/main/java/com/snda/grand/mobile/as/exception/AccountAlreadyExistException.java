package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class AccountAlreadyExistException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5154634814845043705L;

	@Override
	public String getMessage() {
		return "Account already exist.";
	}

	@Override
	public Status getStatus() {
		return Status.CONFLICT;
	}

	@Override
	public String getCode() {
		return "AccountAlreadyExist";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
