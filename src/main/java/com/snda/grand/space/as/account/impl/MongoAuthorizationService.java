package com.snda.grand.space.as.account.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import com.snda.grand.space.as.account.AuthorizationService;
import com.snda.grand.space.as.mongo.model.MongoCollections;
import com.snda.grand.space.as.mongo.model.PojoAuthorization;

public class MongoAuthorizationService implements AuthorizationService {
	
	private final MongoOperations mongoOps;
	
	public MongoAuthorizationService(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
	}

	@Override
	public List<PojoAuthorization> getAuthorizationsByUid(String uid) {
		List<PojoAuthorization> authorizations = mongoOps.find(
				query(where(MongoCollections.Authorization.UID).is(uid)),
				PojoAuthorization.class,
				MongoCollections.AUTHORIZATION_COLLECTION_NAME);
		return authorizations;
	}
	
	@Override
	public List<PojoAuthorization> getAuthorizationsByAppId(String appId) {
		List<PojoAuthorization> pojoAuthorizations = mongoOps.find(
				query(where(MongoCollections.Application.APPID).is(appId)),
				PojoAuthorization.class, MongoCollections.AUTHORIZATION_COLLECTION_NAME);
		return pojoAuthorizations;
	}

	@Override
	public PojoAuthorization getAuthorizationByUidAndAppId(String uid,
			String appId) {
		Query query = new Query();
		query.addCriteria(where(MongoCollections.Authorization.UID).is(uid));
		query.addCriteria(where(MongoCollections.Authorization.APPID).is(appId));
		PojoAuthorization authorization = mongoOps.findOne(query,
				PojoAuthorization.class,
				MongoCollections.AUTHORIZATION_COLLECTION_NAME);
		return authorization;
	}

	@Override
	public PojoAuthorization getAuthorizationByRefreshToken(String refreshToken) {
		PojoAuthorization authorization = mongoOps.findOne(
				query(where(MongoCollections.Authorization.REFRESH_TOKEN).is(
						refreshToken)), PojoAuthorization.class,
				MongoCollections.AUTHORIZATION_COLLECTION_NAME);
		return authorization;
	}

	@Override
	public void putAuthorization(PojoAuthorization pojoAuthorization) {
		mongoOps.insert(pojoAuthorization,
				MongoCollections.AUTHORIZATION_COLLECTION_NAME);
	}

	@Override
	public void removeAuthorizationsByAppId(String appId) {
		mongoOps.remove(query(where(MongoCollections.Authorization.APPID).is(appId)),
				MongoCollections.AUTHORIZATION_COLLECTION_NAME);
	}

	@Override
	public void removeAuthorizationByUidAndAppId(String uid, String appId) {
		Query query = new Query();
		query.addCriteria(where(MongoCollections.Application.APPID).is(appId))
			 .addCriteria(where(MongoCollections.Application.OWNER).is(uid));
		mongoOps.remove(query, MongoCollections.APPLICATION_COLLECTION_NAME);
	}

}
