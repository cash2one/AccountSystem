package com.snda.grand.space.as.rest;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.utils.OAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.snda.grand.space.as.exception.AccountOAuthProblemException;
import com.snda.grand.space.as.rest.util.Constants;


@Provider
@Component
public class AccountOAuthProblemExceptionMapper implements
		ExceptionMapper<AccountOAuthProblemException> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountOAuthProblemExceptionMapper.class);

	@Override
	public Response toResponse(AccountOAuthProblemException exception) {
		try {
			if (exception.getApi().equalsIgnoreCase("authorize")) {
				String redirectUri = exception.getOauthProblem().getRedirectUri();
				if (OAuthUtils.isEmpty(redirectUri)) {
					redirectUri = Constants.DEFAULT_AUTHORIZE_SUCCESS_REDIRECT_URI;
				}
				OAuthResponse res = OAuthASResponse
						.errorResponse(HttpServletResponse.SC_FOUND)
						.error(exception.getOauthProblem())
						.location(redirectUri)
						.buildQueryMessage();
				LOGGER.error("OAuthProblemException : {}", res.getBody());
				URI location = new URI(res.getLocationUri());
				return Response
						.status(res.getResponseStatus())
						.location(location)
						.build();
			} else {
				OAuthResponse res = OAuthASResponse
						.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(exception.getOauthProblem().getError())
						.setErrorDescription(exception.getOauthProblem().getDescription())
						.setErrorUri(exception.getOauthProblem().getUri())
						.buildJSONMessage();
				LOGGER.error("OAuthProblemException : {}", res.getBody());
				return Response
						.status(res.getResponseStatus())
						.entity(res.getBody())
						.type(MediaType.TEXT_PLAIN_TYPE)
						.build();
			}
		} catch (Exception e) {
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity("We encountered an internal error. Please try again.")
					.build();
		}
	}

}
