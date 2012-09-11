package com.snda.grand.space.as.rest.util;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import com.google.common.collect.Lists;
import com.snda.grand.space.as.exception.DomainMismatchException;
import com.snda.grand.space.as.exception.InvalidEmailException;
import com.snda.grand.space.as.exception.InvalidWebSiteException;
import com.snda.grand.space.as.exception.NotAuthorizedException;
import com.snda.grand.space.as.exception.SignatureMisatchException;
import com.snda.grand.space.as.mongo.internal.model.Accessor;
import com.snda.grand.space.as.mongo.model.MongoCollections;
import com.snda.grand.space.as.mongo.model.PojoAccount;
import com.snda.grand.space.as.mongo.model.PojoApplication;
import com.snda.grand.space.as.mongo.model.PojoAuthorization;
import com.snda.grand.space.as.mongo.model.PojoCode;
import com.snda.grand.space.as.mongo.model.PojoToken;
import com.snda.grand.space.as.util.MD5;
import com.snda.grand.space.as.util.Merchant;

public final class Preconditions {
	
	private static final String DOT_SPLITER = "\\.";
	
	public static PojoAccount getAccountBySndaId(MongoOperations mongoOps,
			String sndaId) {
		PojoAccount account = mongoOps.findOne(
				query(where(MongoCollections.Account.SNDA_ID).is(sndaId)),
				PojoAccount.class, MongoCollections.ACCOUNT_COLLECTION_NAME);
		return account;
	}
	
	public static PojoAccount getAccountByUid(MongoOperations mongoOps, 
			String uid) {
		PojoAccount account = mongoOps.findOne(
				query(where(MongoCollections.Account.UID).is(uid)),
				PojoAccount.class, MongoCollections.ACCOUNT_COLLECTION_NAME);
		return account;
	}
	
	public static PojoApplication getApplicationByAppId(MongoOperations mongoOps, 
			String appId) {
		PojoApplication application = mongoOps.findOne(
				query(where(MongoCollections.Application.APPID).is(appId)),
				PojoApplication.class, MongoCollections.APPLICATION_COLLECTION_NAME);
		return application;
	}
	
	public static PojoAuthorization getAuthorizationByRefreshToken(MongoOperations mongoOps, 
			String refreshToken) {
		PojoAuthorization authorization = mongoOps.findOne(
				query(where(MongoCollections.Authorization.REFRESH_TOKEN).is(
						refreshToken)), PojoAuthorization.class,
				MongoCollections.AUTHORIZATION_COLLECTION_NAME);
		return authorization;
	}
	
	public static PojoAuthorization getAuthorizationByUidAndAppId(MongoOperations mongoOps,
			String uid, String appId) {
		Query query = new Query();
		query.addCriteria(where(MongoCollections.Authorization.UID).is(uid));
		query.addCriteria(where(MongoCollections.Authorization.APPID).is(appId));
		PojoAuthorization authorization = mongoOps.findOne(query,
				PojoAuthorization.class,
				MongoCollections.AUTHORIZATION_COLLECTION_NAME);
		return authorization;
	}
	
	public static void insertAuthorization(MongoOperations mongoOps,
			PojoAuthorization pojoAuthorization) {
		mongoOps.insert(pojoAuthorization,
				MongoCollections.AUTHORIZATION_COLLECTION_NAME);
	}
	
	public static List<PojoAuthorization> getAuthorizationsByUid(MongoOperations mongoOps,
			String uid) {
		List<PojoAuthorization> authorizations = mongoOps.find(
				query(where(MongoCollections.Authorization.UID).is(uid)),
				PojoAuthorization.class,
				MongoCollections.AUTHORIZATION_COLLECTION_NAME);
		return authorizations;
	}
	
	public static PojoCode getCode(MongoOperations mongoOps, String code) {
		PojoCode pojoCode = mongoOps.findOne(query(where(MongoCollections.Code.CODE).is(code)),
				PojoCode.class, MongoCollections.CODE_COLLECTION_NAME);
		return pojoCode;
	}
	
	public static void insertCode(MongoOperations mongoOps, String code, String redirectUri, String uid, String appId) {
		PojoCode pojoCode = new PojoCode(code, redirectUri, uid, appId,
				System.currentTimeMillis());
		mongoOps.insert(pojoCode, MongoCollections.CODE_COLLECTION_NAME);
	}
	
	public static void deleteCode(MongoOperations mongoOps, String code) {
		mongoOps.remove(query(where(MongoCollections.Code.CODE).is(code)), MongoCollections.CODE_COLLECTION_NAME);
	}
	
	public static PojoToken getTokenByAccessToken(MongoOperations mongoOps, String accessToken) {
		PojoToken pojoToken = mongoOps.findOne(
				query(where(MongoCollections.Token.ACCESS_TOKEN).is(accessToken)),
				PojoToken.class, MongoCollections.TOKEN_COLLECTION_NAME);
		return pojoToken;
	}
	
	public static void insertAccessToken(MongoOperations mongoOps, PojoToken pojoToken) {
		mongoOps.insert(pojoToken, MongoCollections.TOKEN_COLLECTION_NAME);
	}
	
	public static void insertAccessToken(MongoOperations mongoOps, String refreshToken, String accessToken, long creationTime) {
		PojoToken pojoToken = new PojoToken(refreshToken, accessToken, creationTime,
				creationTime + MongoCollections.ACCESS_TOKEN_EXPIRE_TIME);
		insertAccessToken(mongoOps, pojoToken);
	}
	

	public static void deleteTokenByAccessToken(MongoOperations mongoOps,
			String accessToken) {
		mongoOps.remove(
				query(where(MongoCollections.Token.ACCESS_TOKEN).is(accessToken)),
				MongoCollections.TOKEN_COLLECTION_NAME);
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
				throw new DomainMismatchException();
			}
		} else {
			throw new DomainMismatchException();
		}
	}
	
	public static void basicAuthorizationValidate(String auth, String appKey, String appSecret) {
		if (auth == null) {
			throw new NotAuthorizedException();
		}
		String stringToSign = appKey + ":" + appSecret;
		if (!auth.equals("Basic "
						+ Base64.encodeBase64String(stringToSign.getBytes()))) {
			throw new SignatureMisatchException();
		}
	}
	
	public static Accessor getAccessor(MongoOperations mongoOps, String accessKey, String secretKey) {
		Query query = new Query()
							.addCriteria(where(MongoCollections.Accessor.ACCESS_KEY).is(accessKey))
							.addCriteria(where(MongoCollections.Accessor.SECRET_KEY).is(secretKey));
		return mongoOps.findOne(query, Accessor.class, MongoCollections.ACCESSOR_COLLECTION_NAME);
	}
	
	public static List<String> getQueriesFromQueryString(String queryString) {
		return Lists.newArrayList(queryString.split("&"));
	}
	
	public static List<String> getSdoValidateCanonicalQueryList(Merchant merchant, List<String> queryList) {
		List<String> canonicalList = Lists.newArrayList();
		for (String query : queryList) {
			if (isSdoValidateQueryParam(query)) {
				canonicalList.add(query);
			}
		}
		canonicalList.add(SdoValidateParams.AREA_ID + "=" + merchant.getAreaId());
		canonicalList.add(SdoValidateParams.APP_ID + "=" + merchant.getAppId());
		canonicalList.add(SdoValidateParams.CUSTOM_SECURITY_LEVEL + "=" + merchant.getCustomSecurityLevel());
		canonicalList.add(SdoValidateParams.MERCHANT_NAME + "=" + merchant.getMerchantName());
		canonicalList.add(SdoValidateParams.SIGNATURE_METHOD + "=" + merchant.getSignatureMethod());
		canonicalList.add(SdoValidateParams.TIMESTAMP + "=" + System.currentTimeMillis() / 1000);
		
		return canonicalList;
	}
	
	public static boolean isSdoValidateQueryParam(String query) {
		if (query == null) {
			return false;
		} else if (query.startsWith("ticket=")) {
			return true;
		}
		return false;
	}
	
	public static String makeSignedSdoValidateUrl(Merchant merchant, List<String> canonicalQueryList) {
		String signedUrl = "http://hps.sdo.com/cas/validate.signature?";
		StringBuilder sb = new StringBuilder();
		Collections.sort(canonicalQueryList);
		for (String query : canonicalQueryList) {
			signedUrl = signedUrl + query + "&";
			sb.append(query);
		}
		sb.append(merchant.getMd5SecretKey());
		return signedUrl + "signature=" + MD5.hexDigest(sb.toString().getBytes());
	}
	
}
