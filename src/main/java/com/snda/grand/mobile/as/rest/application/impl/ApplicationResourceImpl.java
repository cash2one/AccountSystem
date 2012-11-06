package com.snda.grand.mobile.as.rest.application.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkAppDescription;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkAppStatus;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkAppid;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkOwner;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkScope;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkUid;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.snda.grand.mobile.as.exception.InvalidWebSiteException;
import com.snda.grand.mobile.as.processor.ApplicationResourceProcessor;
import com.snda.grand.mobile.as.rest.application.ApplicationResource;
import com.snda.grand.mobile.as.rest.model.Application;
import com.snda.grand.mobile.as.rest.model.Authorization;
import com.snda.grand.mobile.as.rest.util.Rule;


@Service
@Path("application")
public class ApplicationResourceImpl implements ApplicationResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationResourceImpl.class);
	
	private final ApplicationResourceProcessor applicationResourceProcessor;
	
	public ApplicationResourceImpl(ApplicationResourceProcessor applicationResourceProcessor) {
		checkNotNull(applicationResourceProcessor, "ApplicationResourceProcessor is null.");
		this.applicationResourceProcessor = applicationResourceProcessor;
		
		LOGGER.info("ApplicationResourceImpl initialized.");
	}

	@Override
	@POST
	@Path("create/{appid}")
	public Application create(@PathParam("appid") String appId, 
			@QueryParam("uid") String uid, 
			@QueryParam("app_description") String appDescription,
			@QueryParam("app_status") String appStatus, 
			@QueryParam("publisher_name") String publisherName,
			@QueryParam("scope") String scope, 
			@QueryParam("website") String website) {
		checkUid(uid);
		checkAppDescription(appDescription);
		checkAppStatus(appStatus);
		checkScope(scope);
		if (website != null &&  !Rule.checkDomain(website)) {
			LOGGER.info("Invalid website.");
			throw new InvalidWebSiteException();
		}
		if (scope == null) {
			LOGGER.info("Scope is empty and set as app.");
			scope = "app";
		}
		if (publisherName == null) {
			LOGGER.info("Publisher name is empty and set as appId.");
			publisherName = appId;
		}
		return applicationResourceProcessor.create(appId, uid, appDescription,
				appStatus, publisherName, scope, website);
	}

	@Override
	@GET
	@Path("authorized/{appid}")
	public List<Authorization> listAuthorized(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner) {
		checkOwner(owner);
		return applicationResourceProcessor.listAuthorized(appId, owner);
	}

	@Override
	@GET
	@Path("status/{appid}")
	public Application status(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner) {
		checkOwner(owner);
		return applicationResourceProcessor.status(appId, owner);
	}

	@Override
	@POST
	@Path("modify/{appid}")
	public Application modify(@PathParam("appid") String appId, 
			@QueryParam("appid") String modifiedAppId,
			@QueryParam("owner") String owner, 
			@QueryParam("app_description") String appDescription,
			@QueryParam("website") String website,
			@QueryParam("publisher_name") String publisherName) {
		checkOwner(owner);
		if (website != null &&  !Rule.checkDomain(website)) {
			LOGGER.info("Invalid website.");
			throw new InvalidWebSiteException();
		}
		return applicationResourceProcessor.modify(appId, modifiedAppId, owner, appDescription, website, publisherName);
	}

	@Override
	@POST
	@Path("status/{appid}")
	public Application changeStatus(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner, 
			@QueryParam("app_status") String appStatus) {
		checkOwner(owner);
		checkAppStatus(appStatus);
		return applicationResourceProcessor.changeStatus(appId, owner, appStatus);
	}

	@Override
	@DELETE
	@Path("token")
	public void cancelAuthorization(@QueryParam("uid") String uid, 
			@QueryParam("appid") String appId) {
		checkUid(uid);
		checkAppid(appId);
		applicationResourceProcessor.cancelAuthorization(uid, appId);
	}

	@Override
	@DELETE
	@Path("delete/{appid}")
	public void delete(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner) {
		checkOwner(owner);
		applicationResourceProcessor.delete(appId, owner);
	}
	
}
