package com.snda.grand.space.as.account.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snda.grand.space.as.account.AccountService;
import com.snda.grand.space.as.account.AuthorizationService;
import com.snda.grand.space.as.mongo.model.PojoAuthorization;
import com.snda.grand.space.as.rest.model.Account;

public class WWWAccountService implements AccountService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WWWAccountService.class);
	
	private static final String WWW_APPID = "www";
	private static final String WWW_AUTHORIZATION_SCOPE = "full";
	private final AccountService accountService;
	private final AuthorizationService authorizationService;
	
	public WWWAccountService(AccountService accountService, 
			AuthorizationService authorizationService) {
		this.accountService = accountService;
		this.authorizationService = authorizationService;
	}

	@Override
	public Account putAccount(String sndaId, String uid, String usernameNorm,
			String displayName, String email, String locale, boolean available,
			long creationTime, long modifiedTime) {
		Account account = accountService.putAccount(sndaId, uid, usernameNorm,
				displayName, email, locale, available, creationTime,
				modifiedTime);
		PojoAuthorization pojoAuthorization = new PojoAuthorization(uid, WWW_APPID,
				UUID.randomUUID().toString(), System.currentTimeMillis(), WWW_AUTHORIZATION_SCOPE);
		authorizationService.putAuthorization(pojoAuthorization);
		return account;
	}

	@Override
	public Account updateAccount(String sndaId, String uid,
			String usernameNorm, String displayName, String email,
			String locale, boolean available, long creationTime,
			long modifiedTime) {
		return accountService.updateAccount(sndaId, uid, usernameNorm,
				displayName, email, locale, available, creationTime,
				modifiedTime);
	}

	@Override
	public Account getAccountBySndaId(String sndaId) {
		return accountService.getAccountBySndaId(sndaId);
	}

	@Override
	public Account getAccountByUid(String uid) {
		return accountService.getAccountByUid(uid);
	}

	@Override
	public void deleteAccountBySndaId(String sndaId) {
		accountService.deleteAccountBySndaId(sndaId);
	}

}
