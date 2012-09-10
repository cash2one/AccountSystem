package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class NoSuchRefreshTokenException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1194921727816417326L;

	@Override
	public String getMessage() {
		return "No such refresh token.";
	}

	@Override
	public Status getStatus() {
		return Status.NOT_FOUND;
	}

	@Override
	public String getCode() {
		return "NoSuchRefreshToken";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
