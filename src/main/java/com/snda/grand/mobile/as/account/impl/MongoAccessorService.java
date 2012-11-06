package com.snda.grand.mobile.as.account.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import com.snda.grand.mobile.as.account.AccessorService;
import com.snda.grand.mobile.as.mongo.internal.model.Accessor;
import com.snda.grand.mobile.as.mongo.model.MongoCollections;

public class MongoAccessorService implements AccessorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoAccessorService.class);
	private MongoOperations mongoOps;
	
	public MongoAccessorService(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
		LOGGER.info("MongoAccessorService initialized.");
	}
	
	@Override
	public Accessor getAccessor(String accessKey, String secretKey) {
		Query query = new Query()
							.addCriteria(where(MongoCollections.Accessor.ACCESS_KEY).is(accessKey))
							.addCriteria(where(MongoCollections.Accessor.SECRET_KEY).is(secretKey));
		return mongoOps.findOne(query, Accessor.class, MongoCollections.ACCESSOR_COLLECTION_NAME);
	}

}
