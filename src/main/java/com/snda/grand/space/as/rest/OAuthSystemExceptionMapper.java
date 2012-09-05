package com.snda.grand.space.as.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;

public class OAuthSystemExceptionMapper implements
		ExceptionMapper<OAuthSystemException> {

	@Override
	public Response toResponse(OAuthSystemException exception) {
		return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.entity("We encountered an internal error. Please try again.")
				.build();
	}

}
