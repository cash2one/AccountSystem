package com.snda.grand.mobile.as.account;

import com.snda.grand.mobile.as.mongo.internal.model.Accessor;


public interface AccessorService {

	Accessor getAccessor(String accessKey, String secretKey);
	
}
