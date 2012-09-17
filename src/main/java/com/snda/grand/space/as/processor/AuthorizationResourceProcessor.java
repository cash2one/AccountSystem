package com.snda.grand.space.as.processor;

import com.snda.grand.space.as.rest.model.Authorization;

public interface AuthorizationResourceProcessor {

	Authorization getAuthorizationByUidAndAppId(String uid, String appId);
	
}
