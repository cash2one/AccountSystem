package com.snda.grand.mobile.as.account.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Charsets;
import com.snda.grand.mobile.as.account.TokenService;
import com.snda.grand.mobile.as.memcached.Memcached;
import com.snda.grand.mobile.as.mongo.model.PojoToken;
import com.snda.grand.mobile.as.util.Loggers;

public class MemcachedTokenService implements TokenService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MemcachedTokenService.class);
	
	private final TokenService tokenService;
	private final Memcached memcached;
	
	private int expiration;
	
	public MemcachedTokenService(TokenService tokenService,
			Memcached memcached) {
		this.tokenService = tokenService;
		this.memcached = memcached;
		LOGGER.info("MemcachedTokenService initialized.");
	}

	@Override
	public PojoToken putToken(String refreshToken, String accessToken,
			long creationTime, long expireTime) {
		PojoToken pojoToken = tokenService.putToken(refreshToken, accessToken, creationTime, expireTime);
		setAccessTokenCache(pojoToken);
		LOGGER.info("Set access token cache : {}", accessToken);
		setRefreshTokenCache(pojoToken);
		LOGGER.info("Set refresh token cache : {}", refreshToken);
		return pojoToken;
	}

	@Override
	public PojoToken getTokenByAccessToken(String accessToken) {
		PojoToken cached = getAccessTokenCache(accessToken);
		if (cached != null) {
			LOGGER.info("Cache hit access token : {}", accessToken);
			return cached;
		}
		PojoToken pojoToken = tokenService.getTokenByAccessToken(accessToken);
		if (pojoToken != null) {
			setAccessTokenCache(pojoToken);
			setRefreshTokenCache(pojoToken);
		}
		return pojoToken;
	}

	@Override
	public void deleteTokenByAccessToken(String accessToken) {
		deleteAccessTokenCache(accessToken);
		LOGGER.info("Delete cache access token : {}", accessToken);
		tokenService.deleteTokenByAccessToken(accessToken);
	}

	@Override
	public void deleteTokenByRefreshToken(String refreshToken) {
		deleteRefreshTokenCache(refreshToken);
		LOGGER.info("Delete cache refresh token : {}", refreshToken);
		tokenService.deleteTokenByRefreshToken(refreshToken);
	}
	
	private PojoToken getAccessTokenCache(String accessToken) {
		String memcachedAccessTokenId = memcachedAccessTokenId(accessToken);
		try {
			return memcached.get(PojoToken.class, memcachedAccessTokenId);
		} catch (Exception e) {
			warnAccessToken("Get", memcachedAccessTokenId, e);
			return null;
		}
	}
	
	private void setAccessTokenCache(PojoToken token) {
		String memcachedAccessTokenId = memcachedAccessTokenId(token.getAccessToken());
		try {
			memcached.set(memcachedAccessTokenId, token, expiration);
		} catch (Exception e) {
			warnAccessToken("Set", memcachedAccessTokenId, e);
		}
	}
	
	private void setRefreshTokenCache(PojoToken token) {
		String memcachedRefreshTokenId = memcachedRefreshTokenId(token.getRefreshToken());
		try {
			memcached.set(memcachedRefreshTokenId, token, expiration);
		} catch (Exception e) {
			warnRefreshToken("Set", memcachedRefreshTokenId, e);
		}
	}
	
	private void deleteAccessTokenCache(String accessToken) {
		String memcachedAccessTokenId = memcachedAccessTokenId(accessToken);
		try {
			memcached.delete(memcachedAccessTokenId);
		} catch (Exception e) {
			warnAccessToken("Delete", memcachedAccessTokenId, e);
		}
	}
	
	private void deleteRefreshTokenCache(String refreshToken) {
		String memcachedRefreshTokenId = memcachedRefreshTokenId(refreshToken);
		try {
			memcached.delete(memcachedRefreshTokenId);
		} catch (Exception e) {
			warnRefreshToken("Delete", memcachedRefreshTokenId, e);
		}
	}

	private String memcachedAccessTokenId(String accessToken) {
		try {
			return "AccessToken#" + URLEncoder.encode(accessToken, Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private String memcachedRefreshTokenId(String refreshToken) {
		try {
			return "RefreshToken#" + URLEncoder.encode(refreshToken, Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private void warnAccessToken(String operation, String memcachedId, Throwable e) {
		Loggers.WARNING_LOGGER.error(MessageFormat.format("AccessTokenCache: {0} cache {1} failed, cause: {2}.",
				operation, 
				memcachedId, 
				e.toString()), 
				e);
	}
	
	private void warnRefreshToken(String operation, String memcachedId, Throwable e) {
		Loggers.WARNING_LOGGER.error(MessageFormat.format("RefreshTokenCache: {0} cache {1} failed, cause: {2}.",
				operation, 
				memcachedId, 
				e.toString()), 
				e);
	}
	
	@Required
	public void setExpiration(int expiration) {
		this.expiration = expiration;
	}

}
