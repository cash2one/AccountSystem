package com.snda.grand.space.as.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.snda.grand.space.as.exception.ApplicationWebException;

public class ApplicationWebExceptionMapper implements
		ExceptionMapper<ApplicationWebException> {

	@Override
	public Response toResponse(ApplicationWebException exception) {
		return Response
				.status(exception.getStatus())
				.entity(exception.getEntity())
				.type(MediaType.TEXT_PLAIN_TYPE)
				.build();
	}

}
