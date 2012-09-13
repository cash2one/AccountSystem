package com.snda.grand.space.as.account.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import com.snda.grand.space.as.account.AccessorService;
import com.snda.grand.space.as.mongo.internal.model.Accessor;
import com.snda.grand.space.as.mongo.model.MongoCollections;

public class MongoAccessorService implements AccessorService {

	private MongoOperations mongoOps;
	
	public MongoAccessorService(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
	}
	
	@Override
	public Accessor getAccessor(String accessKey, String secretKey) {
		Query query = new Query()
							.addCriteria(where(MongoCollections.Accessor.ACCESS_KEY).is(accessKey))
							.addCriteria(where(MongoCollections.Accessor.SECRET_KEY).is(secretKey));
		return mongoOps.findOne(query, Accessor.class, MongoCollections.ACCESSOR_COLLECTION_NAME);
	}

}
