package com.snda.grand.space.as.rest.oauth2;

import com.snda.grand.space.as.rest.model.Validation;


public interface ValidateResource {

	Validation validate(String accessToken);
	
}
