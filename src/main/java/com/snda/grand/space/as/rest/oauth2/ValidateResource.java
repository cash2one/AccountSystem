package com.snda.grand.space.as.rest.oauth2;

import javax.servlet.http.HttpServletRequest;

import com.snda.grand.space.as.rest.model.Validation;


public interface ValidateResource {

	Validation validate(HttpServletRequest request);
	
}
