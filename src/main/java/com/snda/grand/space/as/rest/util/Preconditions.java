package com.snda.grand.space.as.rest.util;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import com.snda.grand.space.as.exception.DomainDoesNotMatchException;
import com.snda.grand.space.as.exception.InvalidEmailException;
import com.snda.grand.space.as.exception.InvalidWebSiteException;
import com.snda.grand.space.as.exception.NotAuthorizedException;
import com.snda.grand.space.as.exception.SignatureDoesNotMatchException;
import com.snda.grand.space.as.mongo.model.Collections;
import com.snda.grand.space.as.mongo.model.PojoAccount;
import com.snda.grand.space.as.mongo.model.PojoApplication;
import com.snda.grand.space.as.mongo.model.PojoAuthorization;
import com.snda.grand.space.as.mongo.model.PojoCode;
import com.snda.grand.space.as.mongo.model.PojoToken;

public final class Preconditions {
	
	private static final String DOT_SPLITER = "\\.";
	
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
	
	public static PojoAuthorization getAuthorizationByRefreshToken(MongoOperations mongoOps, 
			String refreshToken) {
		PojoAuthorization authorization = mongoOps.findOne(
				query(where(Collections.Authorization.REFRESH_TOKEN).is(
						refreshToken)), PojoAuthorization.class,
				Collections.AUTHORIZATION_COLLECTION_NAME);
		return authorization;
	}
	
	public static PojoAuthorization getAuthorizationByUidAndAppId(MongoOperations mongoOps,
			String uid, String appId) {
		Query query = new Query();
		query.addCriteria(where(Collections.Authorization.UID).is(uid));
		query.addCriteria(where(Collections.Authorization.APPID).is(appId));
		PojoAuthorization authorization = mongoOps.findOne(query,
				PojoAuthorization.class,
				Collections.AUTHORIZATION_COLLECTION_NAME);
		return authorization;
	}
	
	public static void insertAuthorization(MongoOperations mongoOps,
			PojoAuthorization pojoAuthorization) {
		mongoOps.insert(pojoAuthorization,
				Collections.AUTHORIZATION_COLLECTION_NAME);
	}
	
	public static List<PojoAuthorization> getAuthorizationsByUid(MongoOperations mongoOps,
			String uid) {
		List<PojoAuthorization> authorizations = mongoOps.find(
				query(where(Collections.Authorization.UID).is(uid)),
				PojoAuthorization.class,
				Collections.AUTHORIZATION_COLLECTION_NAME);
		return authorizations;
	}
	
	public static PojoCode getCode(MongoOperations mongoOps, String code) {
		PojoCode pojoCode = mongoOps.findOne(query(where(Collections.Code.CODE).is(code)),
				PojoCode.class, Collections.CODE_COLLECTION_NAME);
		return pojoCode;
	}
	
	public static void insertCode(MongoOperations mongoOps, String code, String redirectUri, String uid, String appId) {
		PojoCode pojoCode = new PojoCode(code, redirectUri, uid, appId,
				System.currentTimeMillis());
		mongoOps.insert(pojoCode, Collections.CODE_COLLECTION_NAME);
	}
	
	public static void deleteCode(MongoOperations mongoOps, String code) {
		mongoOps.remove(query(where(Collections.Code.CODE).is(code)), Collections.CODE_COLLECTION_NAME);
	}
	
	public static PojoToken getTokenByAccessToken(MongoOperations mongoOps, String accessToken) {
		PojoToken pojoToken = mongoOps.findOne(
				query(where(Collections.Token.ACCESS_TOKEN).is(accessToken)),
				PojoToken.class, Collections.TOKEN_COLLECTION_NAME);
		return pojoToken;
	}
	
	public static void insertAccessToken(MongoOperations mongoOps, PojoToken pojoToken) {
		mongoOps.insert(pojoToken, Collections.TOKEN_COLLECTION_NAME);
	}
	
	public static void insertAccessToken(MongoOperations mongoOps, String refreshToken, String accessToken, long creationTime) {
		PojoToken pojoToken = new PojoToken(refreshToken, accessToken, creationTime,
				creationTime + Collections.ACCESS_TOKEN_EXPIRE_TIME);
		insertAccessToken(mongoOps, pojoToken);
	}
	

	public static void deleteTokenByAccessToken(MongoOperations mongoOps,
			String accessToken) {
		mongoOps.remove(
				query(where(Collections.Token.ACCESS_TOKEN).is(accessToken)),
				Collections.TOKEN_COLLECTION_NAME);
	}
	
	public static void insertRefreshToken(MongoOperations mongoOps, String uid,
			String appId, String refreshToken) {
		PojoAuthorization authorization = new PojoAuthorization(uid, appId,
				refreshToken, System.currentTimeMillis());
		insertAuthorization(mongoOps, authorization);
	}
	
	public static void checkEmail(String email) {
		if (!Rule.checkEmail(email)) {
			throw new InvalidEmailException();
		}
	}
	
	public static void checkDomain(String domain) {
		if (!Rule.checkDomain(domain)) {
			throw new InvalidWebSiteException();
		}
	}
	
	public static void checkSubDomain(String domain, String subDomain) {
		String subDomainGrep = null;
		Matcher matcher = Rule.SUB_DOMAIN_PATTERN.matcher(subDomain);
		if (matcher.find()) {
			subDomainGrep = StringUtils.strip(matcher.group(), "/:");
			String[] domainSplit = domain.split(DOT_SPLITER);
			String[] subDomainSplit = subDomainGrep.split(DOT_SPLITER);
			if (!domainSplit[domainSplit.length - 1]
					.equalsIgnoreCase(subDomainSplit[subDomainSplit.length - 1])
					|| !domainSplit[domainSplit.length - 2]
							.equalsIgnoreCase((subDomainSplit[subDomainSplit.length - 2]))) {
				throw new DomainDoesNotMatchException();
			}
		} else {
			throw new DomainDoesNotMatchException();
		}
	}
	
	public static void basicAuthorizationValidate(String auth, String appKey, String appSecret) {
		if (auth == null) {
			throw new NotAuthorizedException();
		}
		String stringToSign = appKey + ":" + appSecret;
		if (!auth.equals("Basic "
						+ Base64.encodeBase64String(stringToSign.getBytes()))) {
			throw new SignatureDoesNotMatchException();
		}
	}

}
