package com.snda.grand.space.as.rest.oauth2;

import javax.servlet.http.HttpServletRequest;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;

import com.snda.grand.space.as.rest.model.Validation;


public interface ValidateResource {

	Validation validate(HttpServletRequest request) throws OAuthSystemException;
	
}
