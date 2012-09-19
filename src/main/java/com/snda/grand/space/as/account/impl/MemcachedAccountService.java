package com.snda.grand.space.as.account.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Charsets;
import com.snda.grand.space.as.account.AccountService;
import com.snda.grand.space.as.memcached.Memcached;
import com.snda.grand.space.as.rest.model.Account;
import com.snda.grand.space.as.util.Loggers;

public class MemcachedAccountService implements AccountService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MemcachedAccountService.class);
	
	private final AccountService accountService;
	private final Memcached memcached;
	
	private int expiration;
	
	public MemcachedAccountService(AccountService accountService,
			Memcached memcached) {
		this.accountService = accountService;
		this.memcached = memcached;
	}

	@Override
	public Account putAccount(String sndaId, String uid, String usernameNorm,
			String displayName, String email, String locale, boolean available,
			long creationTime, long modifiedTime) {
		Account account = accountService.putAccount(sndaId, uid, usernameNorm,
				displayName, email, locale, available, creationTime,
				modifiedTime);
		setCache(account, sndaId);
		return account;
	}
	
	@Override
	public Account updateAccount(String sndaId, String uid,
			String usernameNorm, String displayName, String email,
			String locale, boolean available, long creationTime,
			long modifiedTime) {
		Account account = accountService.updateAccount(sndaId, uid,
				usernameNorm, displayName, email, locale, available,
				creationTime, modifiedTime);
		setCache(account, sndaId);
		return account;
	}

	@Override
	public Account getAccountBySndaId(String sndaId) {
		Account cached = getCache(sndaId);
		if (cached != null) {
			LOGGER.info("Cache hit.");
			return cached;
		}
		Account account = accountService.getAccountBySndaId(sndaId);
		if (account != null) {
			setCache(account, sndaId);
		}
		return account;
	}
	
	@Override
	public Account getAccountByUid(String uid) {
		return accountService.getAccountByUid(uid);
	}
	
	@Override
	public void deleteAccountBySndaId(String sndaId) {
		deleteCache(sndaId);
		accountService.deleteAccountBySndaId(sndaId);
	}
	
	private Account getCache(String sndaId) {
		String memcachedId = memcachedId(sndaId);
		try {
			return memcached.get(Account.class, memcachedId);
		} catch (Exception e) {
			warn("Get", memcachedId, e);
			return null;
		}
	}
	
	private void setCache(Account account, String sndaId) {
		String memcachedId = memcachedId(sndaId);
		try {
			memcached.set(memcachedId, account, expiration);
		} catch (Exception e) {
			warn("Set", memcachedId, e);
		}
	}
	
	private void deleteCache(String sndaId) {
		String memcachedId = memcachedId(sndaId);
		try {
			memcached.delete(memcachedId);
		} catch (Exception e) {
			warn("Delete", memcachedId, e);
		}
	}

	private String memcachedId(String sndaId) {
		try {
			return "Account#" + URLEncoder.encode(sndaId, Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private void warn(String operation, String memcachedId, Throwable e) {
		Loggers.WARNING_LOGGER.error(MessageFormat.format("AccountCache: {0} cache {1} failed, cause: {2}.",
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
