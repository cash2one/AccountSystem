package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class SignatureDoesNotMatchException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2043429470823436089L;

	public SignatureDoesNotMatchException() {
		super(Response
				.status(Status.FORBIDDEN)
				.entity("The request signature we calculated does not match the signature you provided.")
				.build());
	}

	@Override
	public String getMessage() {
		return "The request signature we calculated does not match the signature you provided.";
	}

}
