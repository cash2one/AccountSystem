package com.snda.grand.mobile.as.exception;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;

public class ASOAuthProblemException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5613165562927836073L;
	private final OAuthProblemException oauthProblem;
	private final String api;
	
	public ASOAuthProblemException(OAuthProblemException oauthProblem, String api) {
		this.oauthProblem = oauthProblem;
		this.api = api;
	}

	public OAuthProblemException getOauthProblem() {
		return oauthProblem;
	}

	public String getApi() {
		return api;
	}
	
}
