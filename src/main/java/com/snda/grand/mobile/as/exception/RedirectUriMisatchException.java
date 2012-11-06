package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class RedirectUriMisatchException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6853148743098603007L;

	@Override
	public String getMessage() {
		return "Redirect URI mismatch";
	}

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "redirect_uri_mismatch";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
