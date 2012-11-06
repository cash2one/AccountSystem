package com.snda.grand.mobile.as.processor.impl;

import static com.snda.grand.mobile.as.rest.util.Preconditions.checkAccountExist;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkAccountNotNull;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.snda.grand.mobile.as.account.AccountService;
import com.snda.grand.mobile.as.account.ApplicationService;
import com.snda.grand.mobile.as.account.AuthorizationService;
import com.snda.grand.mobile.as.mongo.model.PojoAuthorization;
import com.snda.grand.mobile.as.processor.AccountResourceProcessor;
import com.snda.grand.mobile.as.rest.model.Account;
import com.snda.grand.mobile.as.rest.model.Application;
import com.snda.grand.mobile.as.rest.model.Authorization;
import com.snda.grand.mobile.as.rest.util.ApplicationKeys;

public class AccountResourceProcessorImpl implements AccountResourceProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountResourceProcessorImpl.class);
	
	private final AccountService accountService;
	private final ApplicationService applicationService;
	private final AuthorizationService authorizationService;
	
	public AccountResourceProcessorImpl(AccountService accountService,
			ApplicationService applicationService,
			AuthorizationService authorizationService) {
		this.accountService = accountService;
		this.applicationService = applicationService;
		this.authorizationService = authorizationService;
		LOGGER.info("AccountResourceProcessorImpl initialized.");
	}
	
	@Override
	public Account create(String sndaId, String usernameNorm, String displayName, String email,
			String locale, boolean available) {
		checkAccountExist(accountService.getAccountBySndaId(sndaId));
		long creationTime = System.currentTimeMillis();
		return accountService.putAccount(sndaId, 
				ApplicationKeys.generateAccessKeyId(), 
				usernameNorm, displayName, email, 
				locale, available, creationTime,
				creationTime);
	}

	@Override
	public Account modify(String sndaId, String displayName, String email,
			String locale) {
		Account account = checkAccountNotNull(accountService.getAccountBySndaId(sndaId));
		String modifiedDisplayName = isBlank(displayName) ? account.getDisplayName() : displayName;
		String modifiedEmail = isBlank(email) ? account.getEmail() : email;
		String modifiedLocale = isBlank(locale) ? account.getLocale() : locale;
		return accountService.updateAccount(sndaId, account.getUid(), account
				.getUsernameNorm(), modifiedDisplayName, modifiedEmail,
				modifiedLocale, account.isAvailable(), account
						.getCreationTime().getMillis(), System
						.currentTimeMillis());
	}

	@Override
	public Account available(String sndaId, boolean available) {
		Account account = checkAccountNotNull(accountService.getAccountBySndaId(sndaId));
		return accountService.updateAccount(sndaId, account.getUid(), account
				.getUsernameNorm(), account.getDisplayName(), account
				.getEmail(), account.getLocale(), available, account
				.getCreationTime().getMillis(), System.currentTimeMillis());
	}

	@Override
	public Account status(String sndaId) {
		return checkAccountNotNull(accountService.getAccountBySndaId(sndaId));
	}

	@Override
	public List<Application> applications(String sndaId) {
		Account account = checkAccountNotNull(accountService.getAccountBySndaId(sndaId));
		return applicationService.listApplications(account.getUid());
	}

	@Override
	public List<Authorization> listAuthorizations(String sndaId) {
		Account account = checkAccountNotNull(accountService.getAccountBySndaId(sndaId));
		List<PojoAuthorization> pojoAuthorizations = authorizationService
				.getAuthorizationsByUid(account.getUid());
		List<Authorization> authorizations = Lists.newArrayList();
		for (PojoAuthorization pojoAuthorization : pojoAuthorizations) {
			Application application = applicationService.getApplicationByAppId(pojoAuthorization.getAppId());
			if (application != null) {
				application.setScope(pojoAuthorization.getAuthorizedScope());
				authorizations.add(getAuthorization(application, pojoAuthorization));
			}
		}
		return authorizations;
	}
	
	@Override
	public void delete(String sndaId) {
		checkAccountNotNull(accountService.getAccountBySndaId(sndaId));
		accountService.deleteAccountBySndaId(sndaId);
	}
	
	private Authorization getAuthorization(Application application,
			PojoAuthorization pojoAuthorization) {
		Authorization authorization = null;
		if (application.getAppid().equals(pojoAuthorization.getAppId())) {
			authorization = new Authorization();
			authorization.setAppId(application.getAppid());
			authorization.setAuthorizedTime(new DateTime(pojoAuthorization.getAuthorizedTime()));
			authorization.setPublisherName(application.getPublisherName());
			authorization.setRefreshToken(pojoAuthorization.getRefreshToken());
			authorization.setScope(application.getScope());
			authorization.setUid(pojoAuthorization.getUid());
		}
		return authorization;
	}

}
