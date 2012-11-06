package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class SignatureMisatchException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2043429470823436089L;

	@Override
	public String getMessage() {
		return "The request signature we calculated does not match the signature you provided.";
	}

	@Override
	public Status getStatus() {
		return Status.FORBIDDEN;
	}

	@Override
	public String getCode() {
		return "SignatureMisatch";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
