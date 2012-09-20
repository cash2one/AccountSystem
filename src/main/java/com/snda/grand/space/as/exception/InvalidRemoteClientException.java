package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class InvalidRemoteClientException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5470015756626423646L;

	@Override
	public Status getStatus() {
		return Status.FORBIDDEN;
	}

	@Override
	public String getCode() {
		return "InvalidRemoteClient";
	}

	@Override
	public String getMessage() {
		return "Invalid remote client.";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
