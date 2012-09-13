package com.snda.grand.space.as.account;

import com.snda.grand.space.as.mongo.internal.model.Accessor;


public interface AccessorService {

	Accessor getAccessor(String accessKey, String secretKey);
	
}
