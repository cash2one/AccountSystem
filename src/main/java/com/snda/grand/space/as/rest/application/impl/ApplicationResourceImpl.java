package com.snda.grand.space.as.rest.application.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.snda.grand.space.as.exception.AccessDeniedException;
import com.snda.grand.space.as.exception.ApplicationAlreadyExistException;
import com.snda.grand.space.as.exception.InvalidAppDescriptionException;
import com.snda.grand.space.as.exception.InvalidAppStatusException;
import com.snda.grand.space.as.exception.InvalidRequestParamsException;
import com.snda.grand.space.as.exception.InvalidUidException;
import com.snda.grand.space.as.exception.NoSuchAccountException;
import com.snda.grand.space.as.exception.NoSuchApplicationException;
import com.snda.grand.space.as.exception.NotModifiedException;
import com.snda.grand.space.as.mongo.model.Collections;
import com.snda.grand.space.as.mongo.model.PojoApplication;
import com.snda.grand.space.as.mongo.model.PojoAuthorization;
import com.snda.grand.space.as.rest.application.ApplicationResource;
import com.snda.grand.space.as.rest.model.Application;
import com.snda.grand.space.as.rest.model.Authorization;
import com.snda.grand.space.as.rest.util.ApplicationKeys;
import com.snda.grand.space.as.rest.util.Preconditions;


@Service
@Path("application")
public class ApplicationResourceImpl implements ApplicationResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationResourceImpl.class);
	private static MongoOperations mongoOps;
	
	public ApplicationResourceImpl(MongoOperations mongoOperations) {
		checkNotNull(mongoOperations, "MongoTemplate is null.");
		LOGGER.info("ApplicationResourceImpl initialized.");
		ApplicationResourceImpl.mongoOps = mongoOperations;
	}

	@Override
	@POST
	@Path("create/{appid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Application create(@PathParam("appid") String appId, 
			@QueryParam("uid") String uid, 
			@QueryParam("app_description") String appDescription,
			@QueryParam("app_status") String appStatus, 
			@QueryParam("scope") String scope, 
			@QueryParam("website") String website) {
		checkUid(uid);
		checkAppDescription(appDescription);
		checkAppStatus(appStatus);
		if (Preconditions.getAccountByUid(mongoOps, uid) == null) {
			throw new NoSuchAccountException();
		}
		if (Preconditions.getApplicationByAppId(mongoOps, appId) != null) {
			throw new ApplicationAlreadyExistException();
		}
		long creationTime = System.currentTimeMillis();
		PojoApplication application = new PojoApplication(appId, appDescription, appStatus,
				ApplicationKeys.generateAccessKeyId(),
				ApplicationKeys.generateSecretAccessKey(), scope, website,
				creationTime, creationTime, uid);
		mongoOps.insert(application, Collections.APPLICATION_COLLECTION_NAME);
		return application.getApplication();
	}

	@Override
	@GET
	@Path("authorized/{appid}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Authorization> listAuthorized(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner) {
		checkOwner(owner);
		if (Preconditions.getAccountByUid(mongoOps, owner) == null) {
			throw new NoSuchAccountException();
		}
		PojoApplication application = Preconditions.getApplicationByAppId(mongoOps, appId);
		if (application == null) {
			throw new NoSuchApplicationException();
		}
		if (!owner.equals(application.getOwner())) {
			throw new AccessDeniedException();
		}
		List<PojoAuthorization> authorizations = mongoOps.find(
				query(where(Collections.Application.APPID).is(appId)),
				PojoAuthorization.class, Collections.AUTHORIZATION_COLLECTION_NAME);
		return PojoAuthorization.getAuthorizations(authorizations);
	}

	@Override
	@GET
	@Path("status/{appid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Application status(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner) {
		checkOwner(owner);
		if (Preconditions.getAccountByUid(mongoOps, owner) == null) {
			throw new NoSuchAccountException();
		}
		PojoApplication application = Preconditions.getApplicationByAppId(mongoOps, appId);
		if (application == null) {
			throw new NoSuchApplicationException();
		}
		if (!owner.equals(application.getOwner())) {
			throw new AccessDeniedException();
		}
		return application.getApplication();
	}

	@Override
	@POST
	@Path("modify/{appid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Application modify(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner, 
			@QueryParam("app_description") String appDescription,
			@QueryParam("website") String website) {
		checkOwner(owner);
		if (Preconditions.getAccountByUid(mongoOps, owner) == null) {
			throw new NoSuchAccountException();
		}
		PojoApplication application = Preconditions.getApplicationByAppId(mongoOps, appId);
		if (application == null) {
			throw new NoSuchApplicationException();
		}
		if (!owner.equals(application.getOwner())) {
			throw new AccessDeniedException();
		}
		if (isBlank(appDescription) && isBlank(website)) {
			throw new NotModifiedException();
		}
		long modifiedTime = System.currentTimeMillis();
		Query query = new Query();
		query.addCriteria(where(Collections.Application.APPID).is(appId))
			 .addCriteria(where(Collections.Application.OWNER).is(owner));
		Update update = new Update();
		String modifiedAppDescription = isBlank(appDescription) ? application.getAppDescription() : appDescription;
		String modifiedWebsite = isBlank(website) ? application.getWebsite() : website;
		update.set(Collections.Application.APP_DESCRIPTION, modifiedAppDescription)
			  .set(Collections.Application.WEBSITE, modifiedWebsite)
			  .set(Collections.Application.MODIFIED_TIME, modifiedTime);
		mongoOps.updateFirst(query, update, Collections.APPLICATION_COLLECTION_NAME);
		application.setAppDescription(modifiedAppDescription)
				   .setWebsite(modifiedWebsite)
				   .setModifiedTime(modifiedTime);
		return application.getApplication();
	}

	@Override
	@POST
	@Path("status/{appid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Application changeStatus(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner, 
			@QueryParam("app_status") String appStatus) {
		checkOwner(owner);
		checkAppStatus(appStatus);
		if (Preconditions.getAccountByUid(mongoOps, owner) == null) {
			throw new NoSuchAccountException();
		}
		PojoApplication application = Preconditions.getApplicationByAppId(mongoOps, appId);
		if (application == null) {
			throw new NoSuchApplicationException();
		}
		if (!owner.equals(application.getOwner())) {
			throw new AccessDeniedException();
		}
		if (application.getAppStatus().equals(appStatus)) {
			throw new NotModifiedException();
		}
		long modifiedTime = System.currentTimeMillis();
		Query query = new Query();
		query.addCriteria(where(Collections.Application.APPID).is(appId))
			 .addCriteria(where(Collections.Application.OWNER).is(owner))
			 .addCriteria(where(Collections.Application.APP_STAUTS).is(appStatus));
		Update update = new Update();
		update.set(Collections.Application.APP_STAUTS, appStatus)
			  .set(Collections.Application.MODIFIED_TIME, modifiedTime);
		mongoOps.updateFirst(query, update,
				Collections.APPLICATION_COLLECTION_NAME);
		application.setAppStatus(appStatus)
				   .setModifiedTime(modifiedTime);
		return application.getApplication();
	}

	@Override
	@DELETE
	@Path("token")
	@Produces(MediaType.APPLICATION_JSON)
	public void cancelAuthorization(@QueryParam("uid") String uid, 
			@QueryParam("appid") String appId) {
		checkUid(uid);
		checkAppid(appId);
		if (Preconditions.getAccountByUid(mongoOps, uid) == null) {
			throw new NoSuchAccountException();
		}
		if (Preconditions.getApplicationByAppId(mongoOps, appId) == null) {
			throw new NoSuchApplicationException();
		}
		Query query = new Query();
		query.addCriteria(where(Collections.Authorization.UID).is(uid))
			 .addCriteria(where(Collections.Authorization.APPID).is(appId));
		mongoOps.remove(query, Collections.AUTHORIZATION_COLLECTION_NAME);
	}

	@Override
	@DELETE
	@Path("delete/{appid}")
	@Produces(MediaType.APPLICATION_JSON)
	public void delete(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner) {
		checkOwner(owner);
		if (Preconditions.getAccountByUid(mongoOps, owner) == null) {
			throw new NoSuchAccountException();
		}
		PojoApplication application = Preconditions.getApplicationByAppId(mongoOps, appId);
		if (application == null) {
			throw new NoSuchApplicationException();
		}
		if (!owner.equals(application.getOwner())) {
			throw new AccessDeniedException();
		}
		cancelAllAuthorization(appId);
		Query query = new Query();
		query.addCriteria(where(Collections.Application.APPID).is(appId))
			 .addCriteria(where(Collections.Application.OWNER).is(owner));
		mongoOps.remove(query, Collections.APPLICATION_COLLECTION_NAME);
	}
	
	
	private void cancelAllAuthorization(String appId) {
		mongoOps.remove(query(where(Collections.Authorization.APPID).is(appId)),
				Collections.AUTHORIZATION_COLLECTION_NAME);
	}
	
	private void checkOwner(String owner) {
		if (isBlank(owner)) {
			throw new InvalidRequestParamsException("owner");
		}
	}
	
	private void checkAppid(String appid) {
		if (isBlank(appid)) {
			throw new InvalidRequestParamsException("appid");
		}
	}
	
	private void checkUid(String uid) {
		if (isBlank(uid)) {
			throw new InvalidUidException();
		}
	}

	private void checkAppDescription(String appDescription) {
		if (isBlank(appDescription)) {
			throw new InvalidAppDescriptionException();
		}
	}

	private void checkAppStatus(String appStatus) {
		if (appStatus == null 
				|| (!"development".equalsIgnoreCase(appStatus)
						&& !"release".equalsIgnoreCase(appStatus))) {
			throw new InvalidAppStatusException();
		}
	}
	
}
