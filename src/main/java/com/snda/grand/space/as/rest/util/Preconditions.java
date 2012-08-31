package com.snda.grand.space.as.rest.util;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.data.mongodb.core.MongoOperations;

import com.snda.grand.space.as.mongo.model.Collections;
import com.snda.grand.space.as.mongo.model.PojoAccount;
import com.snda.grand.space.as.mongo.model.PojoApplication;

public final class Preconditions {

	public static PojoAccount getAccountBySndaId(MongoOperations mongoOps,
			String sndaId) {
		PojoAccount account = mongoOps.findOne(
				query(where(Collections.Account.SNDA_ID).is(sndaId)),
				PojoAccount.class, Collections.ACCOUNT_COLLECTION_NAME);
		return account;
	}
	
	public static PojoAccount getAccountByUid(MongoOperations mongoOps, 
			String uid) {
		PojoAccount account = mongoOps.findOne(
				query(where(Collections.Account.UID).is(uid)),
				PojoAccount.class, Collections.ACCOUNT_COLLECTION_NAME);
		return account;
	}
	
	public static PojoApplication getApplicationByAppId(MongoOperations mongoOps, 
			String appId) {
		PojoApplication application = mongoOps.findOne(
				query(where(Collections.Application.APPID).is(appId)),
				PojoApplication.class, Collections.APPLICATION_COLLECTION_NAME);
		return application;
	}

}
