package com.snda.grand.mobile.as.processor;

import com.snda.grand.mobile.as.rest.model.Authorization;

public interface AuthorizationResourceProcessor {

	Authorization getAuthorizationByUidAndAppId(String uid, String appId);
	
}
