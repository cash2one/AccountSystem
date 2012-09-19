package com.snda.grand.space.as.processor.impl;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.snda.grand.space.as.account.AccountService;
import com.snda.grand.space.as.account.ApplicationService;
import com.snda.grand.space.as.account.AuthorizationService;
import com.snda.grand.space.as.account.TokenService;
import com.snda.grand.space.as.exception.AccessDeniedException;
import com.snda.grand.space.as.exception.ApplicationAlreadyExistException;
import com.snda.grand.space.as.exception.NoSuchAccountException;
import com.snda.grand.space.as.exception.NoSuchApplicationException;
import com.snda.grand.space.as.exception.NoSuchAuthorizationException;
import com.snda.grand.space.as.exception.NotModifiedException;
import com.snda.grand.space.as.mongo.model.PojoAuthorization;
import com.snda.grand.space.as.processor.ApplicationResourceProcessor;
import com.snda.grand.space.as.rest.model.Account;
import com.snda.grand.space.as.rest.model.Application;
import com.snda.grand.space.as.rest.model.Authorization;
import com.snda.grand.space.as.rest.util.ApplicationKeys;

public class ApplicationResourceProcessorImpl implements
		ApplicationResourceProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationResourceProcessorImpl.class);
	
	private final AccountService accountService;
	private final ApplicationService applicationService;
	private final AuthorizationService authorizationService;
	private final TokenService tokenService;
	
	public ApplicationResourceProcessorImpl(AccountService accountService,
			ApplicationService applicationService,
			AuthorizationService authorizationService,
			TokenService tokenService) {
		this.accountService = accountService;
		this.applicationService = applicationService;
		this.authorizationService = authorizationService;
		this.tokenService = tokenService;
	}

	@Override
	public Application create(String appId, String uid, String appDescription,
			String appStatus, String publisherName, String scope, String website) {
		Account account = accountService.getAccountByUid(uid);
		if (account == null) {
			throw new NoSuchAccountException();
		}
		Application application = applicationService.getApplicationByAppId(appId);
		if (application != null) {
			throw new ApplicationAlreadyExistException();
		}
		long creationTime = System.currentTimeMillis();
		return applicationService.putApplication(appId, uid,
				ApplicationKeys.generateAccessKeyId(),
				ApplicationKeys.generateSecretAccessKey(), appDescription,
				appStatus, publisherName, scope, website, creationTime,
				creationTime);
	}

	@Override
	public List<Authorization> listAuthorized(String appId, String owner) {
		Account account = accountService.getAccountByUid(owner);
		if (account == null) {
			throw new NoSuchAccountException();
		}
		Application application = applicationService.getApplicationByAppId(appId);
		if (application == null) {
			throw new NoSuchApplicationException();
		}
		if (!owner.equals(application.getOwner())) {
			throw new AccessDeniedException();
		}
		List<PojoAuthorization> pojoAuthorizations = authorizationService.getAuthorizationsByAppId(appId);
		List<Authorization> authorizations = Lists.newArrayList();
		if (pojoAuthorizations != null) {
			for (PojoAuthorization pojoAuthorization : pojoAuthorizations) {
				authorizations
						.add(pojoAuthorization.getAuthorization(
								application.getPublisherName()));
			}
		}
		return authorizations;
	}

	@Override
	public Application status(String appId, String owner) {
		Account account = accountService.getAccountByUid(owner);
		if (account == null) {
			throw new NoSuchAccountException();
		}
		Application application = applicationService.getApplicationByAppId(appId);
		if (application == null) {
			throw new NoSuchApplicationException();
		}
		if (!owner.equals(application.getOwner())) {
			throw new AccessDeniedException();
		}
		return application;
	}

	@Override
	public Application modify(String appId, String modifiedAppId, 
			String owner, String appDescription, String website, String publisherName) {
		Account account = accountService.getAccountByUid(owner);
		if (account == null) {
			throw new NoSuchAccountException();
		}
		Application application = applicationService.getApplicationByAppId(appId);
		if (application == null) {
			throw new NoSuchApplicationException();
		}
		if (!owner.equals(application.getOwner())) {
			throw new AccessDeniedException();
		}
		if (modifiedAppId != null && !appId.equalsIgnoreCase(modifiedAppId)
				&& applicationService.getApplicationByAppId(modifiedAppId) != null) {
			throw new ApplicationAlreadyExistException();
		}
		if (isBlank(appDescription) 
				&& isBlank(website)
				&& isBlank(modifiedAppId)
				&& isBlank(publisherName)) {
			throw new NotModifiedException();
		}
		long modifiedTime = System.currentTimeMillis();
		String modifiedAppDescription = isBlank(appDescription) ? application.getAppDescription() : appDescription;
		String modifiedWebsite = isBlank(website) ? application.getWebsite() : website;
		return applicationService.updateApplication(appId, modifiedAppId, owner, 
				application.getAppKey(), application.getAppSecret(),
				modifiedAppDescription, application.getAppStatus(),
				application.getPublisherName(), application.getScope(),
				modifiedWebsite, application.getCreationTime().getMillis(),
				modifiedTime);
	}

	@Override
	public Application changeStatus(String appId, String owner, String appStatus) {
		Account account = accountService.getAccountByUid(owner);
		if (account == null) {
			throw new NoSuchAccountException();
		}
		Application application = applicationService.getApplicationByAppId(appId);
		if (application == null) {
			throw new NoSuchApplicationException();
		}
		if (!owner.equals(application.getOwner())) {
			throw new AccessDeniedException();
		}
		return applicationService.updateApplication(appId, appId, owner, 
				application.getAppKey(), application.getAppSecret(),
				application.getAppDescription(), appStatus,
				application.getPublisherName(), application.getScope(),
				application.getWebsite(), application.getCreationTime()
						.getMillis(), System.currentTimeMillis());
	}

	@Override
	public void cancelAuthorization(String uid, String appId) {
		PojoAuthorization pojoAuthorization = authorizationService.getAuthorizationByUidAndAppId(uid, appId);
		if (pojoAuthorization == null) {
			throw new NoSuchAuthorizationException();
		}
		Account account = accountService.getAccountByUid(uid);
		if (account == null) {
			throw new NoSuchAccountException();
		}
		Application application = applicationService.getApplicationByAppId(appId);
		if (application == null) {
			throw new NoSuchApplicationException();
		}
		tokenService.deleteTokenByRefreshToken(pojoAuthorization.getRefreshToken());
		authorizationService.removeAuthorizationByUidAndAppId(uid, appId);
	}

	@Override
	public void delete(String appId, String owner) {
		Account account = accountService.getAccountByUid(owner);
		if (account == null) {
			throw new NoSuchAccountException();
		}
		Application application = applicationService.getApplicationByAppId(appId);
		if (application == null) {
			throw new NoSuchApplicationException();
		}
		if (!owner.equals(application.getOwner())) {
			throw new AccessDeniedException();
		}
		List<PojoAuthorization> pojoAuthorizations = authorizationService
				.getAuthorizationsByAppId(appId);
		if (pojoAuthorizations != null) {
			for (PojoAuthorization pojoAuthorization : pojoAuthorizations) {
				tokenService.deleteTokenByRefreshToken(pojoAuthorization.getRefreshToken());
			}
		}
		authorizationService.removeAuthorizationsByAppId(appId);
		applicationService.deleteApplication(appId);
	}

}
