package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class InvalidRefreshTokenException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -588583265594384728L;

	public InvalidRefreshTokenException() {
		super(Response
				.status(Status.BAD_REQUEST)
				.entity("Invalid refresh token")
				.build());
	}

	@Override
	public String getMessage() {
		return "Invalid refresh token";
	}

}
