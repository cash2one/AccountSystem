package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class NoSuchAccountException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3034042003891435351L;

	public NoSuchAccountException() {
		super(Response
				.status(Status.NOT_FOUND)
				.entity("No such account.")
				.build());
	}

	@Override
	public String getMessage() {
		return "No such account.";
	}

}
