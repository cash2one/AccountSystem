package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidUidException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 771325027886979167L;

	@Override
	public String getMessage() {
		return "Invalid uid.";
	}

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "InvalidUid";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
