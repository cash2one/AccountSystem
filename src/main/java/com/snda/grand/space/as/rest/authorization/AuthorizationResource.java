package com.snda.grand.space.as.rest.authorization;

import com.snda.grand.space.as.rest.model.Authorization;

public interface AuthorizationResource {
	
	Authorization getAuthorization(String uid, String appId);

}
