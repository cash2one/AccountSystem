package com.snda.grand.space.as.processor.impl;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.snda.grand.space.as.account.AccountService;
import com.snda.grand.space.as.account.ApplicationService;
import com.snda.grand.space.as.account.AuthorizationService;
import com.snda.grand.space.as.exception.AccountAlreadyExistException;
import com.snda.grand.space.as.exception.InvalidEmailException;
import com.snda.grand.space.as.exception.NoSuchAccountException;
import com.snda.grand.space.as.exception.NotModifiedException;
import com.snda.grand.space.as.mongo.model.PojoAuthorization;
import com.snda.grand.space.as.processor.AccountResourceProcessor;
import com.snda.grand.space.as.rest.model.Account;
import com.snda.grand.space.as.rest.model.Application;
import com.snda.grand.space.as.rest.model.Authorization;
import com.snda.grand.space.as.rest.util.ApplicationKeys;
import com.snda.grand.space.as.rest.util.Rule;

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
	}
	
	@Override
	public Account create(String sndaId, String usernameNorm, String displayName, String email,
			String locale, boolean available) {
		if (accountService.getAccountBySndaId(sndaId) != null) {
			throw new AccountAlreadyExistException();
		}
		String uid = ApplicationKeys.generateAccessKeyId();
		long creationTime = System.currentTimeMillis();
		return accountService.putAccount(sndaId, uid, usernameNorm,
				displayName, email, locale, available, creationTime,
				creationTime);
	}

	@Override
	public Account modify(String sndaId, String displayName, String email,
			String locale) {
		Account account = accountService.getAccountBySndaId(sndaId);
		if (account == null) {
			throw new NoSuchAccountException();
		}
		if (isBlank(displayName) && isBlank(email) && isBlank(locale)) {
			throw new NotModifiedException();
		}
		if (email != null && !Rule.checkEmail(email)) {
			throw new InvalidEmailException();
		}
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
		Account account = accountService.getAccountBySndaId(sndaId);
		if (account == null) {
			throw new NoSuchAccountException();
		}
		return accountService.updateAccount(sndaId, account.getUid(), account
				.getUsernameNorm(), account.getDisplayName(), account
				.getEmail(), account.getLocale(), available, account
				.getCreationTime().getMillis(), System.currentTimeMillis());
	}

	@Override
	public Account status(String sndaId) {
		Account account = accountService.getAccountBySndaId(sndaId);
		if (account == null) {
			throw new NoSuchAccountException();
		}
		return account;
	}

	@Override
	public List<Application> applications(String sndaId) {
		Account account = accountService.getAccountBySndaId(sndaId);
		if (account == null) {
			throw new NoSuchAccountException();
		}
		return applicationService.listApplications(account.getUid());
	}

	@Override
	public List<Authorization> listAuthorizations(String sndaId) {
		Account account = accountService.getAccountBySndaId(sndaId);
		if (account == null) {
			throw new NoSuchAccountException();
		}
		List<PojoAuthorization> pojoAuthorizations = authorizationService
				.getAuthorizationsByUid(account.getUid());
		List<Authorization> authorizations = Lists.newArrayList();
		for (PojoAuthorization pojoAuthorization : pojoAuthorizations) {
			Application application = applicationService.getApplicationByAppId(pojoAuthorization.getAppId());
			authorizations.add(getAuthorization(application, pojoAuthorization));
		}
		return authorizations;
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
