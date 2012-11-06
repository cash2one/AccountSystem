package com.snda.grand.mobile.as.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.snda.grand.mobile.as.rest.model.ApplicationError;
import com.snda.grand.mobile.as.rest.model.Error;

public abstract class ApplicationWebException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public abstract Status getStatus();
	
	public abstract String getCode();
	
	public abstract String getMessage();
	
	public abstract MediaType getType();
	
	public Error getError() {
		return new ApplicationError();
	}
	
	public final Error getBody() {
		Error error = getError();
		error.setErrorCode(getCode());
		error.setErrorDescription(getMessage());
		return error;
	}
	
	public final Response toResponse() {
		return Response
				.status(getStatus())
				.entity(getBody())
				.type(getType())
				.build();
	}
	
}
