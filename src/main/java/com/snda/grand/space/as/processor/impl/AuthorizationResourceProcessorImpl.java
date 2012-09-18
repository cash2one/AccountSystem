package com.snda.grand.space.as.processor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snda.grand.space.as.account.AccountService;
import com.snda.grand.space.as.account.ApplicationService;
import com.snda.grand.space.as.account.AuthorizationService;
import com.snda.grand.space.as.exception.NoSuchAccountException;
import com.snda.grand.space.as.exception.NoSuchApplicationException;
import com.snda.grand.space.as.mongo.model.PojoAuthorization;
import com.snda.grand.space.as.processor.AuthorizationResourceProcessor;
import com.snda.grand.space.as.rest.model.Account;
import com.snda.grand.space.as.rest.model.Application;
import com.snda.grand.space.as.rest.model.Authorization;

public class AuthorizationResourceProcessorImpl implements
		AuthorizationResourceProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationResourceProcessorImpl.class);
	
	private final AccountService accountService;
	private final ApplicationService applicationService;
	private final AuthorizationService authorizationService;
	
	public AuthorizationResourceProcessorImpl(AccountService accountService,
			ApplicationService applicationService,
			AuthorizationService authorizationService) {
		this.accountService = accountService;
		this.applicationService = applicationService;
		this.authorizationService = authorizationService;
	}

	@Override
	public Authorization getAuthorizationByUidAndAppId(String uid, String appId) {
		Account account = accountService.getAccountByUid(uid);
		if (account == null) {
			throw new NoSuchAccountException();
		}
		Application application = applicationService.getApplicationByAppId(appId);
		if (application == null) {
			throw new NoSuchApplicationException();
		}
		PojoAuthorization pojoAuthorization = authorizationService.getAuthorizationByUidAndAppId(uid, appId);
		if (pojoAuthorization != null) {
			return pojoAuthorization.getAuthorization(application
					.getPublisherName());
		}
		return null;
	}

}
