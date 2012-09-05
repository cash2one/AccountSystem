package com.snda.grand.space.as.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class RuntimeExceptionMapper implements
		ExceptionMapper<RuntimeException> {

	@Override
	public Response toResponse(RuntimeException exception) {
		return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.entity("We encountered an internal error. Please try again.")
				.build();
	}

}
