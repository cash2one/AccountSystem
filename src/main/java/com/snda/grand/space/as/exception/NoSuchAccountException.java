package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class NoSuchAccountException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3034042003891435351L;

	@Override
	public String getMessage() {
		return "No such account.";
	}

	@Override
	public Status getStatus() {
		return Status.NOT_FOUND;
	}

	@Override
	public String getCode() {
		return "NoSuchAccount";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
