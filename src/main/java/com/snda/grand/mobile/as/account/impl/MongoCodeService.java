package com.snda.grand.mobile.as.account.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;

import com.snda.grand.mobile.as.account.CodeService;
import com.snda.grand.mobile.as.mongo.model.MongoCollections;
import com.snda.grand.mobile.as.mongo.model.PojoCode;

public class MongoCodeService implements CodeService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoCodeService.class);
	private final MongoOperations mongoOps;
	
	public MongoCodeService(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
		LOGGER.info("MongoCodeService initialized.");
	}

	@Override
	public PojoCode putCode(String code, String redirectUri, String uid,
			String appId) {
		PojoCode pojoCode = new PojoCode(code, redirectUri, uid, appId,
				System.currentTimeMillis());
		mongoOps.insert(pojoCode, MongoCollections.CODE_COLLECTION_NAME);
		LOGGER.info("Put code : {}", pojoCode);
		return pojoCode;
	}

	@Override
	public PojoCode getCode(String code) {
		PojoCode pojoCode = mongoOps.findOne(
				query(where(MongoCollections.Code.CODE).is(code)),
				PojoCode.class, MongoCollections.CODE_COLLECTION_NAME);
		LOGGER.info("Get code : {}", pojoCode);
		return pojoCode;
	}

	@Override
	public void deleteCode(String code) {
		mongoOps.remove(query(where(MongoCollections.Code.CODE).is(code)),
				MongoCollections.CODE_COLLECTION_NAME);
		LOGGER.info("Delete code : {}", code);
	}

}
