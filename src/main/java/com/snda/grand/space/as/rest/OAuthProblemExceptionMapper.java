package com.snda.grand.space.as.rest;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;

public class OAuthProblemExceptionMapper implements
		ExceptionMapper<OAuthProblemException> {

	@Override
	public Response toResponse(OAuthProblemException exception) {
		try {
			OAuthResponse res = OAuthASResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
					.setError(exception.getError())
					.setErrorDescription(exception.getDescription())
					.setErrorUri(exception.getUri())
					.buildJSONMessage();
			return Response
					.status(res.getResponseStatus())
					.entity(res.getBody())
					.type(MediaType.APPLICATION_JSON_TYPE)
					.build();
		} catch (OAuthSystemException e) {
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity("We encountered an internal error. Please try again.")
					.build();
		}
	}

}
