package com.snda.grand.space.as.exception;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;

public class ASOAuthUnauthorizedClientException extends ASOAuthProblemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5446662788322113428L;
	
	private static final String ERROR = "unauthorized_client";
	private static final String ERROR_DESCRIPTION = "Unauthorized client";
	
	public ASOAuthUnauthorizedClientException(OAuthProblemException oauthProblem, String api) {
		super(oauthProblem, api);
	}
	
	public static ASOAuthUnauthorizedClientException build(String api) {
		OAuthProblemException oauthProblem = OAuthProblemException.error(ERROR, ERROR_DESCRIPTION);
		return new ASOAuthUnauthorizedClientException(oauthProblem, api);
	}

}
