package com.snda.grand.space.as.account;

import java.util.List;

import com.snda.grand.space.as.mongo.model.PojoAuthorization;

public interface AuthorizationService {

	List<PojoAuthorization> getAuthorizationsByUid(String uid);
	
	List<PojoAuthorization> getAuthorizationsByAppId(String appId);
	
	PojoAuthorization getAuthorizationByRefreshToken(String refreshToken);
	
	PojoAuthorization getAuthorizationByUidAndAppId(String uid, String appId);
	
	void putAuthorization(PojoAuthorization pojoAuthorization);
	
	void updateAuthorizationScope(String refreshToken, String scope);
	
	void removeAuthorizationsByAppId(String appId);
	
	void removeAuthorizationByUidAndAppId(String uid, String appId);
	
}
