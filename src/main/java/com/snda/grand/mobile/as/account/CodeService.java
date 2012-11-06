package com.snda.grand.mobile.as.account;

import com.snda.grand.mobile.as.mongo.model.PojoCode;

public interface CodeService {

	PojoCode putCode(String code, String redirectUri, String uid, String appId);
	
	PojoCode getCode(String code);
	
	void deleteCode(String code);
	
}
