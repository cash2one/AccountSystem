package com.snda.grand.space.as.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.snda.grand.space.as.exception.ApplicationWebException;


@Provider
@Component
public class ApplicationWebExceptionMapper implements
		ExceptionMapper<ApplicationWebException> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationWebExceptionMapper.class);
	
	@Override
	public Response toResponse(ApplicationWebException exception) {
		LOGGER.info("{} caused by : {}", exception.getClass().getSimpleName(), exception.getMessage());

		return Response
				.status(exception.getStatus())
				.entity(exception.getEntity())
				.type(MediaType.TEXT_PLAIN_TYPE)
				.build();
	}
	
}
