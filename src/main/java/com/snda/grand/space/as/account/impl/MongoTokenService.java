package com.snda.grand.space.as.account.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.data.mongodb.core.MongoOperations;

import com.snda.grand.space.as.account.TokenService;
import com.snda.grand.space.as.mongo.model.MongoCollections;
import com.snda.grand.space.as.mongo.model.PojoToken;

public class MongoTokenService implements TokenService {
	
	private final MongoOperations mongoOps;
	
	public MongoTokenService(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
	}

	@Override
	public PojoToken putToken(String refreshToken, String accessToken,
			long creationTime, long expireTime) {
		PojoToken pojoToken = new PojoToken(refreshToken, accessToken, creationTime,
				expireTime);
		mongoOps.insert(pojoToken, MongoCollections.TOKEN_COLLECTION_NAME);
		return pojoToken;
	}

	@Override
	public PojoToken getTokenByAccessToken(String accessToken) {
		PojoToken pojoToken = mongoOps.findOne(
				query(where(MongoCollections.Token.ACCESS_TOKEN).is(accessToken)),
				PojoToken.class, MongoCollections.TOKEN_COLLECTION_NAME);
		return pojoToken;
	}

	@Override
	public void deleteTokenByAccessToken(String accessToken) {
		mongoOps.remove(
				query(where(MongoCollections.Token.ACCESS_TOKEN).is(accessToken)),
				MongoCollections.TOKEN_COLLECTION_NAME);
	}

	@Override
	public void deleteTokenByRefreshToken(String refreshToken) {
		mongoOps.remove(
				query(where(MongoCollections.Token.REFRESH_TOKEN).is(refreshToken)),
				MongoCollections.TOKEN_COLLECTION_NAME);
	}

}
