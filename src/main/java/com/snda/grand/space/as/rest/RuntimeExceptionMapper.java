package com.snda.grand.space.as.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Provider
@Component
public class RuntimeExceptionMapper implements
		ExceptionMapper<RuntimeException> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

	@Override
	public Response toResponse(RuntimeException exception) {
		LOGGER.error("RuntimeException caused by: ", exception);
		return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.entity("We encountered an internal error. Please try again.")
				.build();
	}

}
