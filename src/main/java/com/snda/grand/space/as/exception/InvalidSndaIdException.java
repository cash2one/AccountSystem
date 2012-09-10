package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;


public class InvalidSndaIdException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4769529418394565568L;
	
	@Override
	public String getMessage() {
		return "Invalid sndaId.";
	}

	@Override
	public Status getStatus() {
		return Status.BAD_REQUEST;
	}

	@Override
	public String getCode() {
		return "InvalidSndaId";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

}
