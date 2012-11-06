package com.snda.grand.mobile.as.account;

import com.snda.grand.mobile.as.mongo.model.PojoToken;

public interface TokenService {
	
	PojoToken putToken(String refreshToken, String accessToken, long creationTime, long expireTime);
	
	PojoToken getTokenByAccessToken(String accessToken);
	
	void deleteTokenByAccessToken(String accessToken);
	
	void deleteTokenByRefreshToken(String refreshToken);
	
}
