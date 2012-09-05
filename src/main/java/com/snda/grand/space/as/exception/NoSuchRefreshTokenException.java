package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class NoSuchRefreshTokenException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1194921727816417326L;

	public NoSuchRefreshTokenException() {
		super(Response
				.status(Status.NOT_FOUND)
				.entity("No such refresh token.")
				.build());
	}

	@Override
	public String getMessage() {
		return "No such refresh token.";
	}

}
