package com.snda.grand.space.as.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class AccountAlreadyExistException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5154634814845043705L;

	public AccountAlreadyExistException() {
		super(Response
				.status(Status.CONFLICT)
				.entity("Account already exist.")
				.build());
	}

	@Override
	public String getMessage() {
		return "Account already exist.";
	}

}
