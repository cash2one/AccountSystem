package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class CodeExpiredException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1111392360542126842L;

	@Override
	public String getMessage() {
		return "Code has expired.";
	}

	@Override
	public Status getStatus() {
		return Status.FORBIDDEN;
	}

	@Override
	public String getCode() {
		return "CodeExpired";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
