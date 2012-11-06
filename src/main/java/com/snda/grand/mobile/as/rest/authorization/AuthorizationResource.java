package com.snda.grand.mobile.as.rest.authorization;

import com.snda.grand.mobile.as.rest.model.Authorization;

public interface AuthorizationResource {
	
	Authorization getAuthorization(String uid, String appId);

}
