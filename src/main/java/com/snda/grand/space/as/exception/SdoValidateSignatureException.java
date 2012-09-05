package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class SdoValidateSignatureException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8578746883039449394L;

	public SdoValidateSignatureException() {
		super(Response
				.status(Status.FORBIDDEN)
				.entity("Sdo passport validate error.")
				.build());
	}

	@Override
	public String getMessage() {
		return "Sdo passport validate error.";
	}

}
