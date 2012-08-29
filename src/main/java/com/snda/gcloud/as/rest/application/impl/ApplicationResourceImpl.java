package com.snda.gcloud.as.rest.application.impl;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.snda.gcloud.as.mongo.model.Account;
import com.snda.gcloud.as.mongo.model.Application;
import com.snda.gcloud.as.mongo.model.Collections;
import com.snda.gcloud.as.mongo.model.Token;
import com.snda.gcloud.as.rest.application.ApplicationResource;
import com.snda.gcloud.as.rest.util.ApplicationKeys;
import com.snda.gcloud.as.rest.util.ObjectMappers;


@Service
@Path("application")
public class ApplicationResourceImpl implements ApplicationResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationResourceImpl.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static MongoOperations mongoOps;
	
	public ApplicationResourceImpl(MongoOperations mongoOperations) {
		checkNotNull(mongoOperations, "MongoTemplate is null.");
		LOGGER.info("ApplicationResourceImpl initialized.");
		mongoOps = mongoOperations;
	}

	@Override
	@POST
	@Path("create/{appid}")
	public Response create(@PathParam("appid") String appId, 
			@QueryParam("uid") String uid, 
			@QueryParam("app_description") String appDescription,
			@QueryParam("app_status") String appStatus, 
			@QueryParam("scope") String scope, 
			@QueryParam("website") String website) {
		if (isBlank(uid) || isBlank(appDescription) || isBlank(appStatus)) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Request must contains uid, app_description and app_status params")
					.build();
		}
		if (!checkAccountExist(uid)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such user.")
					.build();
		}
		if (checkApplicationExist(appId)) {
			return Response
					.status(Status.CONFLICT)
					.entity("Application already exists.")
					.build();
		}
		long creationTime = System.currentTimeMillis();
		Application app = new Application(appId, appDescription, appStatus,
				ApplicationKeys.generateAccessKeyId(),
				ApplicationKeys.generateSecretAccessKey(), scope, website,
				creationTime, uid);
		mongoOps.insert(app, Collections.APPLICATION_COLLECTION_NAME);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, app.getModelApplication()))
				.build();
	}

	@Override
	@GET
	@Path("authorized/{appid}")
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
		List<Token> tokens = mongoOps.find(
				query(where(Collections.Application.APPID).is(appId)),
				Token.class, Collections.TOKEN_COLLECTION_NAME);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, Token.getModelTokens(tokens)))
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
		Application application = mongoOps.findOne(
				query(where(Collections.Application.APPID).is(appId)),
				Application.class, Collections.APPLICATION_COLLECTION_NAME);
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
						application.getModelApplication())).build();
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
		Application application = mongoOps.findOne(
				query(where(Collections.Application.APPID).is(appId)),
				Application.class, Collections.APPLICATION_COLLECTION_NAME);
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
		Query query = new Query();
		query.addCriteria(where(Collections.Application.APPID).is(appId))
			 .addCriteria(where(Collections.Application.OWNER).is(owner));
		Update update = new Update();
		String modifiedAppDescription = isBlank(appDescription) ? application.getAppDescription() : appDescription;
		String modifiedWebsite = isBlank(website) ? application.getWebsite() : website;
		update.set(Collections.Application.APP_DESCRIPTION, modifiedAppDescription)
			  .set(Collections.Application.WEBSITE, modifiedWebsite);
		mongoOps.updateFirst(query, update, Collections.APPLICATION_COLLECTION_NAME);
		application.setAppDescription(modifiedAppDescription)
				   .setWebsite(modifiedWebsite);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER,
						application.getModelApplication())).build();
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
		Application application = mongoOps.findOne(
				query(where(Collections.Application.APPID).is(appId)), Application.class,
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
		Query query = new Query();
		query.addCriteria(where(Collections.Application.APPID).is(appId))
			 .addCriteria(where(Collections.Application.OWNER).is(owner))
			 .addCriteria(where(Collections.Application.APP_STAUTS).is(appStatus));
		mongoOps.updateFirst(query, update(Collections.Application.APP_STAUTS, appStatus),
				Collections.APPLICATION_COLLECTION_NAME);
		application.setAppStatus(appStatus);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER,
						application.getModelApplication())).build();
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
		query.addCriteria(where(Collections.Token.UID).is(uid))
			 .addCriteria(where(Collections.Token.APPID).is(appId));
		mongoOps.remove(query, Collections.TOKEN_COLLECTION_NAME);
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
		Application application = mongoOps.findOne(
				query(where(Collections.Token.APPID).is(appId)), Application.class,
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
		Application app = mongoOps.findOne(
				query(where(Collections.Application.APPID).is(appId)),
				Application.class, Collections.APPLICATION_COLLECTION_NAME);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Application : {}", app);
		}
		return app != null;
	}
	
	private boolean checkAccountExist(String uid) {
		Account account = mongoOps.findOne(query(where(Collections.Account.UID)
				.is(uid)), Account.class, Collections.ACCOUNT_COLLECTION_NAME);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Account : {}", account);
		}
		return account != null;
	}
	
	private boolean checkOwner(String appId, String owner) {
		Query query = new Query();
		query.addCriteria(where(Collections.Application.APPID).is(appId));
		query.addCriteria(where(Collections.Application.OWNER).is(owner));
		Application app = mongoOps.findOne(query, Application.class,
				Collections.APPLICATION_COLLECTION_NAME);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Application : {}", app);
		}
		return app != null;
	}
	
	private void cancelAllAuthorization(String appId) {
		mongoOps.remove(query(where(Collections.Token.APPID).is(appId)),
				Collections.TOKEN_COLLECTION_NAME);
	}

}
