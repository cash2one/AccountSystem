package com.snda.grand.space.as.rest.util;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import com.google.common.collect.Lists;
import com.snda.grand.space.as.exception.ASOAuthInvalidScopeException;
import com.snda.grand.space.as.exception.ASOAuthProblemException;
import com.snda.grand.space.as.exception.DomainMismatchException;
import com.snda.grand.space.as.exception.InvalidAppDescriptionException;
import com.snda.grand.space.as.exception.InvalidAppStatusException;
import com.snda.grand.space.as.exception.InvalidAvailableParamException;
import com.snda.grand.space.as.exception.InvalidDisplayNameException;
import com.snda.grand.space.as.exception.InvalidEmailException;
import com.snda.grand.space.as.exception.InvalidLocaleException;
import com.snda.grand.space.as.exception.InvalidRequestParamsException;
import com.snda.grand.space.as.exception.InvalidScopeException;
import com.snda.grand.space.as.exception.InvalidSndaIdException;
import com.snda.grand.space.as.exception.InvalidUidException;
import com.snda.grand.space.as.exception.InvalidUsernameNormException;
import com.snda.grand.space.as.exception.NotAuthorizedException;
import com.snda.grand.space.as.exception.SignatureMisatchException;
import com.snda.grand.space.as.mongo.internal.model.Accessor;
import com.snda.grand.space.as.mongo.model.MongoCollections;
import com.snda.grand.space.as.util.MD5;
import com.snda.grand.space.as.util.Merchant;

public final class Preconditions {
	
	private static final String DOT_SPLITER = "\\.";
	
	public static void checkSndaId(String sndaId) {
		if (isBlank(sndaId)) {
			throw new InvalidSndaIdException();
		}
	}
	
	public static void checkUsernameNorm(String usernameNorm) {
		if (isBlank(usernameNorm)) {
			throw new InvalidUsernameNormException();
		}
	}
	
	public static void checkDisplayName(String displayName) {
		if (isBlank(displayName)) {
			throw new InvalidDisplayNameException();
		}
	}
	
	public static boolean checkAvailableParam(String available) {
		if (available == null 
				|| (!"true".equalsIgnoreCase(available) 
						&& !"false".equalsIgnoreCase(available))) {
			throw new InvalidAvailableParamException();
		}
		return Boolean.valueOf(available);
	}
	
	public static void checkEmail(String email) {
		if (!Rule.checkEmail(email)) {
			throw new InvalidEmailException();
		}
	}
	
	public static String checkLocale(String locale) {
		if (isBlank(locale)) {
			locale = Constants.DEFAULT_LOCALE;
		} else {
			if (!Locales.containsLocale(locale)) {
				throw new InvalidLocaleException();
			}
		}
		return locale;
	}
	
	public static void checkOwner(String owner) {
		if (isBlank(owner)) {
			throw new InvalidRequestParamsException("owner");
		}
	}
	
	public static void checkAppid(String appid) {
		if (isBlank(appid)) {
			throw new InvalidRequestParamsException("appid");
		}
	}
	
	public static void checkUid(String uid) {
		if (isBlank(uid)) {
			throw new InvalidUidException();
		}
	}

	public static void checkAppDescription(String appDescription) {
		if (isBlank(appDescription)) {
			throw new InvalidAppDescriptionException();
		}
	}

	public static void checkAppStatus(String appStatus) {
		if (appStatus == null 
				|| (!"development".equalsIgnoreCase(appStatus)
						&& !"release".equalsIgnoreCase(appStatus))) {
			throw new InvalidAppStatusException();
		}
	}
	
	public static void checkScope(String scope) {
		if (scope != null 
				&& (!"full".equalsIgnoreCase(scope)
						&& !"app".equalsIgnoreCase(scope))) {
			throw new InvalidScopeException();
		}
	}
	
	public static void checkOAuth2Scope(String scope, String api) throws ASOAuthProblemException {
		if (scope != null 
				&& (!"full".equalsIgnoreCase(scope)
						&& !"app".equalsIgnoreCase(scope))) {
			throw ASOAuthInvalidScopeException.build(api);
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
	
	public static String createSdoValidateSignatureUrl(Merchant merchant, String params) {
		List<String> list = Preconditions.getQueriesFromQueryString(params);
		List<String> canonicalList = Preconditions.getSdoValidateCanonicalQueryList(merchant, list);
		return Preconditions.makeSignedSdoValidateUrl(merchant, canonicalList);
	}
	
	public static void responseContentConsume(HttpResponse response) {
		try {
			if (response != null && response.getEntity() != null) {
				EntityUtils.consume(response.getEntity());
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public static String buildUrlWithQuery(String baseUri, Map<String, String> queries) {
		StringBuilder uriBuilder = new StringBuilder(baseUri + "?");
		for (Entry<String, String> entry : queries.entrySet()) {
			if (entry.getValue() != null) {
				uriBuilder.append(entry.getKey())
						  .append("=")
						  .append(entry.getValue())
						  .append("&");
			} else {
				uriBuilder.append(entry.getKey()).append("&");
			}
		}
		return uriBuilder.toString().substring(0, uriBuilder.toString().length() - 1);
	}
	
}
