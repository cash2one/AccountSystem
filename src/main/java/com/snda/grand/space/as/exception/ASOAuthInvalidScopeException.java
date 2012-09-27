package com.snda.grand.space.as.exception;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;

public class ASOAuthInvalidScopeException extends ASOAuthProblemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6579395737293763293L;
	
	private static final String ERROR = "invalid_scope";
	private static final String ERROR_DESCRIPTION = "Invalid scope";

	public ASOAuthInvalidScopeException(OAuthProblemException oauthProblem,
			String api) {
		super(oauthProblem, api);
	}
	
	public static ASOAuthInvalidScopeException build(String api) {
		OAuthProblemException oauthProblem = OAuthProblemException.error(ERROR, ERROR_DESCRIPTION);
		return new ASOAuthInvalidScopeException(oauthProblem, api);
	}

}
