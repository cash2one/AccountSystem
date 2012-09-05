package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class CodeExpiredException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1111392360542126842L;

	public CodeExpiredException() {
		super(Response
				.status(Status.FORBIDDEN)
				.entity("Code has expired.")
				.build());
	}

	@Override
	public String getMessage() {
		return "Code has expired.";
	}

}
