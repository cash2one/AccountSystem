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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.snda.grand.space.as.exception.ApplicationAlreadyExistException;
import com.snda.grand.space.as.exception.InvalidAppDescriptionException;
import com.snda.grand.space.as.exception.InvalidAppStatusException;
import com.snda.grand.space.as.exception.InvalidUidException;
import com.snda.grand.space.as.exception.NoSuchAccountException;
import com.snda.grand.space.as.mongo.model.Collections;
import com.snda.grand.space.as.mongo.model.PojoAccount;
import com.snda.grand.space.as.mongo.model.PojoApplication;
import com.snda.grand.space.as.mongo.model.PojoAuthorization;
import com.snda.grand.space.as.rest.application.ApplicationResource;
import com.snda.grand.space.as.rest.model.Application;
import com.snda.grand.space.as.rest.util.ApplicationKeys;
import com.snda.grand.space.as.rest.util.ObjectMappers;
import com.snda.grand.space.as.rest.util.Preconditions;


@Service
@Path("application")
public class ApplicationResourceImpl implements ApplicationResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationResourceImpl.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
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
	public Response listAuthorized(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner) {
		if (isBlank(owner)) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Request must contains owner param")
					.build();
		}
		if (!checkAccountExist(owner)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such user.")
					.build();
		}
		if (!checkApplicationExist(appId)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such application.")
					.build();
		}
		if (!checkOwner(appId, owner)) {
			return Response
					.status(Status.FORBIDDEN)
					.entity("Access denied.")
					.build();
		}
		List<PojoAuthorization> tokens = mongoOps.find(
				query(where(Collections.Application.APPID).is(appId)),
				PojoAuthorization.class, Collections.AUTHORIZATION_COLLECTION_NAME);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, PojoAuthorization.getAuthorizations(tokens)))
				.build();
	}

	@Override
	@GET
	@Path("status/{appid}")
	public Response status(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner) {
		if (isBlank(owner)) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Request must contains owner param")
					.build();
		}
		if (!checkAccountExist(owner)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such user.")
					.build();
		}
		PojoApplication application = mongoOps.findOne(
				query(where(Collections.Application.APPID).is(appId)),
				PojoApplication.class, Collections.APPLICATION_COLLECTION_NAME);
		if (application == null) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such application.")
					.build();
		}
		if (!application.getOwner().equals(owner)) {
			return Response
					.status(Status.FORBIDDEN)
					.entity("Access denied.")
					.build();
		}
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER,
						application.getApplication())).build();
	}

	@Override
	@POST
	@Path("modify/{appid}")
	public Response modify(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner, 
			@QueryParam("app_description") String appDescription,
			@QueryParam("website") String website) {
		if (isBlank(owner)) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Request must contains owner param")
					.build();
		}
		if (!checkAccountExist(owner)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such user.")
					.build();
		}
		PojoApplication application = mongoOps.findOne(
				query(where(Collections.Application.APPID).is(appId)),
				PojoApplication.class, Collections.APPLICATION_COLLECTION_NAME);
		if (application == null) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such application.")
					.build();
		}
		if (!application.getOwner().equals(owner)) {
			return Response
					.status(Status.FORBIDDEN)
					.entity("Access denied.")
					.build();
		}
		if (isBlank(appDescription) && isBlank(website)) {
			return Response
					.status(Status.NOT_MODIFIED)
					.entity("Application has not been modified.")
					.build();
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
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER,
						application.getApplication())).build();
	}

	@Override
	@POST
	@Path("status/{appid}")
	public Response changeStatus(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner, 
			@QueryParam("app_status") String appStatus) {
		if (isBlank(owner) || isBlank(appStatus)) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Request must contains appid, owner and app_status params")
					.build();
		}
		if (!checkAccountExist(owner)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such user.")
					.build();
		}
		PojoApplication application = mongoOps.findOne(
				query(where(Collections.Application.APPID).is(appId)), PojoApplication.class,
				Collections.APPLICATION_COLLECTION_NAME);
		if (application == null) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such application.")
					.build();
		}
		if (!application.getOwner().equals(owner)) {
			return Response
					.status(Status.FORBIDDEN)
					.entity("Access denied.")
					.build();
		}
		if (application.getAppStatus().equals(appStatus)) {
			return Response
					.status(Status.NOT_MODIFIED)
					.entity("Application has not been modified.")
					.build();
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
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER,
						application.getApplication())).build();
	}

	@Override
	@DELETE
	@Path("token")
	public Response cancelAuthorization(@QueryParam("uid") String uid, 
			@QueryParam("appid") String appId) {
		if (isBlank(uid) || isBlank(appId)) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Request must contains uid and appid param")
					.build();
		}
		if (!checkAccountExist(uid)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such user.")
					.build();
		}
		if (!checkApplicationExist(appId)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such application.")
					.build();
		}
		Query query = new Query();
		query.addCriteria(where(Collections.Authorization.UID).is(uid))
			 .addCriteria(where(Collections.Authorization.APPID).is(appId));
		mongoOps.remove(query, Collections.AUTHORIZATION_COLLECTION_NAME);
		return Response.noContent().build();
	}

	@Override
	@DELETE
	@Path("delete/{appid}")
	public Response delete(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner) {
		if (isBlank(owner)) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Request must contains owner param")
					.build();
		}
		if (!checkAccountExist(owner)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such user.")
					.build();
		}
		PojoApplication application = mongoOps.findOne(
				query(where(Collections.Authorization.APPID).is(appId)), PojoApplication.class,
				Collections.APPLICATION_COLLECTION_NAME);
		if (application == null) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such application.")
					.build();
		}
		if (!application.getOwner().equals(owner)) {
			return Response
					.status(Status.FORBIDDEN)
					.entity("Access denied.")
					.build();
		}
		cancelAllAuthorization(appId);
		Query query = new Query();
		query.addCriteria(where(Collections.Application.APPID).is(appId))
			 .addCriteria(where(Collections.Application.OWNER).is(owner));
		mongoOps.remove(query, Collections.APPLICATION_COLLECTION_NAME);
		return Response.noContent().build();
	}
	
	private boolean checkApplicationExist(String appId) {
		PojoApplication app = mongoOps.findOne(
				query(where(Collections.Application.APPID).is(appId)),
				PojoApplication.class, Collections.APPLICATION_COLLECTION_NAME);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Application : {}", app);
		}
		return app != null;
	}
	
	private boolean checkAccountExist(String uid) {
		PojoAccount account = mongoOps.findOne(query(where(Collections.Account.UID)
				.is(uid)), PojoAccount.class, Collections.ACCOUNT_COLLECTION_NAME);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Account : {}", account);
		}
		return account != null;
	}
	
	private boolean checkOwner(String appId, String owner) {
		Query query = new Query();
		query.addCriteria(where(Collections.Application.APPID).is(appId));
		query.addCriteria(where(Collections.Application.OWNER).is(owner));
		PojoApplication app = mongoOps.findOne(query, PojoApplication.class,
				Collections.APPLICATION_COLLECTION_NAME);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Application : {}", app);
		}
		return app != null;
	}
	
	private void cancelAllAuthorization(String appId) {
		mongoOps.remove(query(where(Collections.Authorization.APPID).is(appId)),
				Collections.AUTHORIZATION_COLLECTION_NAME);
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
