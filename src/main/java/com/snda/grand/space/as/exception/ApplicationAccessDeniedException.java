package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class ApplicationAccessDeniedException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3442442370282852466L;

	@Override
	public String getMessage() {
		return "Application access denied.";
	}

	@Override
	public Status getStatus() {
		return Status.FORBIDDEN;
	}

	@Override
	public String getCode() {
		return "ApplicationAccessDenied";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
