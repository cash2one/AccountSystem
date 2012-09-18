package com.snda.grand.space.as.rest.oauth2;

import javax.servlet.http.HttpServletRequest;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;

import com.snda.grand.space.as.exception.AccountOAuthProblemException;
import com.snda.grand.space.as.rest.model.Token;

public interface TokenResource {

	Token exchangeToken(HttpServletRequest request) throws AccountOAuthProblemException, OAuthSystemException;
	
}
