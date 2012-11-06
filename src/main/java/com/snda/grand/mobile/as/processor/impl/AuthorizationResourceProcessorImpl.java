package com.snda.grand.mobile.as.processor.impl;

import static com.snda.grand.mobile.as.rest.util.Preconditions.checkAccountNotNull;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkApplicationNotNull;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkAuthorizationNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snda.grand.mobile.as.account.AccountService;
import com.snda.grand.mobile.as.account.ApplicationService;
import com.snda.grand.mobile.as.account.AuthorizationService;
import com.snda.grand.mobile.as.processor.AuthorizationResourceProcessor;
import com.snda.grand.mobile.as.rest.model.Application;
import com.snda.grand.mobile.as.rest.model.Authorization;

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
		checkAccountNotNull(accountService.getAccountByUid(uid));
		Application application = checkApplicationNotNull(applicationService.getApplicationByAppId(appId));
		return checkAuthorizationNotNull(
				authorizationService
				.getAuthorizationByUidAndAppId(uid, appId))
				.getAuthorization(application.getPublisherName());
	}

}
