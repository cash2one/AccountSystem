package com.snda.grand.space.as.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OAuthSystemExceptionMapper implements
		ExceptionMapper<OAuthSystemException> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OAuthSystemExceptionMapper.class);

	@Override
	public Response toResponse(OAuthSystemException exception) {
		LOGGER.error("OAuthSystemException caused by: ", exception);
		return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.entity("We encountered an internal error. Please try again.")
				.build();
	}

}
