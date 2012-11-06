package com.snda.grand.mobile.as.processor.impl;

import static com.snda.grand.mobile.as.rest.util.Preconditions.checkAccountNotNull;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkApplicationAccessDenied;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkApplicationExist;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkApplicationNotNull;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkAuthorizationNotNull;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkNotModified;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.snda.grand.mobile.as.account.AccountService;
import com.snda.grand.mobile.as.account.ApplicationService;
import com.snda.grand.mobile.as.account.AuthorizationService;
import com.snda.grand.mobile.as.account.TokenService;
import com.snda.grand.mobile.as.exception.ApplicationAlreadyExistException;
import com.snda.grand.mobile.as.mongo.model.PojoAuthorization;
import com.snda.grand.mobile.as.processor.ApplicationResourceProcessor;
import com.snda.grand.mobile.as.rest.model.Application;
import com.snda.grand.mobile.as.rest.model.Authorization;
import com.snda.grand.mobile.as.rest.util.ApplicationKeys;

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
		LOGGER.info("ApplicationResourceProcessorImpl initialized.");
	}

	@Override
	public Application create(String appId, String uid, String appDescription,
			String appStatus, String publisherName, String scope, String website) {
		checkAccountNotNull(accountService.getAccountByUid(uid));
		checkApplicationExist(applicationService.getApplicationByAppId(appId));
		long creationTime = System.currentTimeMillis();
		return applicationService.putApplication(appId, uid,
				ApplicationKeys.generateAccessKeyId(),
				ApplicationKeys.generateSecretAccessKey(), 
				appDescription, appStatus, publisherName, 
				scope, website, creationTime, creationTime);
	}

	@Override
	public List<Authorization> listAuthorized(String appId, String owner) {
		checkAccountNotNull(accountService.getAccountByUid(owner));
		Application application = checkApplicationNotNull(applicationService.getApplicationByAppId(appId));
		checkApplicationAccessDenied(application, owner);
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
		checkAccountNotNull(accountService.getAccountByUid(owner));
		Application application = checkApplicationNotNull(applicationService.getApplicationByAppId(appId));
		checkApplicationAccessDenied(application, owner);
		return application;
	}

	@Override
	public Application modify(String appId, String modifiedAppId, 
			String owner, String appDescription, String website, String publisherName) {
		checkAccountNotNull(accountService.getAccountByUid(owner));
		Application application = checkApplicationNotNull(applicationService.getApplicationByAppId(appId));
		checkApplicationAccessDenied(application, owner);
		if (modifiedAppId != null && !appId.equalsIgnoreCase(modifiedAppId)
				&& applicationService.getApplicationByAppId(modifiedAppId) != null) {
			throw new ApplicationAlreadyExistException();
		}
		checkNotModified(appDescription, website, modifiedAppId, publisherName);
		long modifiedTime = System.currentTimeMillis();
		String modifiedApplicationId = isBlank(modifiedAppId) ? appId : modifiedAppId;
		String modifiedAppDescription = isBlank(appDescription) ? application.getAppDescription() : appDescription;
		String modifiedWebsite = isBlank(website) ? application.getWebsite() : website;
		String modifiedPublisherName = isBlank(publisherName) ? application.getPublisherName() : publisherName;
		return applicationService.updateApplication(appId, modifiedApplicationId, owner, 
				application.getAppKey(), application.getAppSecret(),
				modifiedAppDescription, application.getAppStatus(),
				modifiedPublisherName, application.getScope(),
				modifiedWebsite, application.getCreationTime().getMillis(),
				modifiedTime);
	}

	@Override
	public Application changeStatus(String appId, String owner, String appStatus) {
		checkAccountNotNull(accountService.getAccountByUid(owner));
		Application application = checkApplicationNotNull(applicationService.getApplicationByAppId(appId));
		checkApplicationAccessDenied(application, owner);
		return applicationService.updateApplication(appId, appId, owner, 
				application.getAppKey(), application.getAppSecret(),
				application.getAppDescription(), appStatus,
				application.getPublisherName(), application.getScope(),
				application.getWebsite(), application.getCreationTime()
						.getMillis(), System.currentTimeMillis());
	}

	@Override
	public void cancelAuthorization(String uid, String appId) {
		PojoAuthorization pojoAuthorization = checkAuthorizationNotNull(authorizationService.getAuthorizationByUidAndAppId(uid, appId));
		checkAccountNotNull(accountService.getAccountByUid(uid));
		checkApplicationNotNull(applicationService.getApplicationByAppId(appId));
		tokenService.deleteTokenByRefreshToken(pojoAuthorization.getRefreshToken());
		authorizationService.removeAuthorizationByUidAndAppId(uid, appId);
	}

	@Override
	public void delete(String appId, String owner) {
		checkAccountNotNull(accountService.getAccountByUid(owner));
		Application application = checkApplicationNotNull(applicationService.getApplicationByAppId(appId));
		checkApplicationAccessDenied(application, owner);
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
