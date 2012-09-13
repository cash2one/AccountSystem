package com.snda.grand.space.as.account.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.data.mongodb.core.MongoOperations;

import com.snda.grand.space.as.account.CodeService;
import com.snda.grand.space.as.mongo.model.MongoCollections;
import com.snda.grand.space.as.mongo.model.PojoCode;

public class MongoCodeService implements CodeService {
	
	private final MongoOperations mongoOps;
	
	public MongoCodeService(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
	}

	@Override
	public PojoCode putCode(String code, String redirectUri, String uid,
			String appId) {
		PojoCode pojoCode = new PojoCode(code, redirectUri, uid, appId,
				System.currentTimeMillis());
		mongoOps.insert(pojoCode, MongoCollections.CODE_COLLECTION_NAME);
		return pojoCode;
	}

	@Override
	public PojoCode getCode(String code) {
		PojoCode pojoCode = mongoOps.findOne(
				query(where(MongoCollections.Code.CODE).is(code)),
				PojoCode.class, MongoCollections.CODE_COLLECTION_NAME);
		return pojoCode;
	}

	@Override
	public void deleteCode(String code) {
		mongoOps.remove(query(where(MongoCollections.Code.CODE).is(code)),
				MongoCollections.CODE_COLLECTION_NAME);
	}

}
