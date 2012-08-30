package com.snda.grand.space.as.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.snda.grand.space.as.exception.InvalidSndaIdException;


@Provider
public class InvalidSndaIdExceptionMapper implements
		ExceptionMapper<InvalidSndaIdException> {

	@Override
	public Response toResponse(InvalidSndaIdException exception) {
		return Response
				.status(Status.BAD_REQUEST)
				.entity(exception.getMessage())
				.type(MediaType.APPLICATION_XML_TYPE)
				.build();
	}

}
