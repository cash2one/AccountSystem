package com.snda.grand.space.as.account;

import com.snda.grand.space.as.mongo.model.PojoCode;

public interface CodeService {

	PojoCode putCode(String code, String redirectUri, String uid, String appId);
	
	PojoCode getCode(String code);
	
	void deleteCode(String code);
	
}
