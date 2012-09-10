package com.snda.grand.space.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class NoSuchSndaIdException extends ApplicationWebException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8596297600556557394L;

	@Override
	public String getMessage() {
		return "No such snda_id.";
	}

	@Override
	public Status getStatus() {
		return Status.NOT_FOUND;
	}

	@Override
	public String getCode() {
		return "NoSuchSndaId";
	}

	@Override
	public MediaType getType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}
	
}
