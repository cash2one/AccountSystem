package com.snda.gcloud.as.rest.application.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

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
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
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
	
	public ApplicationResourceImpl(MongoDbFactory mongoDbFactory) {
		LOGGER.info("ApplicationResourceImpl initialized.");
		mongoOps = new MongoTemplate(mongoDbFactory);
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
		if (uid == null || appDescription == null || appStatus == null) {
			Response.status(Status.BAD_REQUEST)
					.entity("Request must contains uid, app_description and app_status params")
					.build();
		}
		if (checkApplicationExist(appId)) {
			Response.status(Status.CONFLICT).entity("Application already exists.").build();
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
		if (owner == null) {
			Response.status(Status.BAD_REQUEST)
			.entity("Request must contains owner param")
			.build();
		}
		if (!checkUserExist(owner)) {
			Response.status(Status.NOT_FOUND).entity("No such user.").build();
		}
		if (!checkApplicationExist(appId)) {
			Response.status(Status.NOT_FOUND).entity("No such application.").build();
		}
		if (!checkOwner(appId, owner)) {
			Response.status(Status.FORBIDDEN).entity("Access denied.").build();
		}
		List<Token> tokens = mongoOps.find(query(where("appid").is(appId)), Token.class,
				Collections.TOKEN_COLLECTION_NAME);
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
		if (owner == null) {
			Response.status(Status.BAD_REQUEST)
			.entity("Request must contains owner param")
			.build();
		}
		if (!checkUserExist(owner)) {
			Response.status(Status.NOT_FOUND).entity("No such user.").build();
		}
		Application application = mongoOps.findOne(
				query(where("appid").is(appId)), Application.class,
				Collections.APPLICATION_COLLECTION_NAME);
		if (application == null) {
			Response.status(Status.NOT_FOUND).entity("No such application.").build();
		}
		if (!application.getOwner().equals(owner)) {
			Response.status(Status.FORBIDDEN).entity("Access denied.").build();
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
		if (owner == null) {
			Response.status(Status.BAD_REQUEST)
			.entity("Request must contains owner param")
			.build();
		}
		if (!checkUserExist(owner)) {
			Response.status(Status.NOT_FOUND).entity("No such user.").build();
		}
		Application application = mongoOps.findOne(
				query(where("appid").is(appId)), Application.class,
				Collections.APPLICATION_COLLECTION_NAME);
		if (application == null) {
			Response.status(Status.NOT_FOUND).entity("No such application.").build();
		}
		if (!application.getOwner().equals(owner)) {
			Response.status(Status.FORBIDDEN).entity("Access denied.").build();
		}
		if ( (appDescription == null && website == null)
				|| (application.getAppDescription().equals(appDescription)
						&& application.getWebsite().equals(website)) ) {
			Response.status(Status.NOT_MODIFIED)
					.entity("Application has not been modified.").build();
		}
		Query query = new Query();
		query.addCriteria(where("appid").is(appId))
			 .addCriteria(where("owner").is(owner));
		Update update = new Update();
		update.set("app_description", appDescription)
			  .set("website", website);
		mongoOps.updateFirst(query, update, Collections.APPLICATION_COLLECTION_NAME);
		application.setAppDescription(appDescription)
				   .setWebsite(website);
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
		if (owner == null || appStatus == null) {
			Response.status(Status.BAD_REQUEST)
					.entity("Request must contains appid, owner and app_status params")
					.build();
		}
		if (!checkUserExist(owner)) {
			Response.status(Status.NOT_FOUND).entity("No such user.").build();
		}
		Application application = mongoOps.findOne(
				query(where("appid").is(appId)), Application.class,
				Collections.APPLICATION_COLLECTION_NAME);
		if (application == null) {
			Response.status(Status.NOT_FOUND).entity("No such application.").build();
		}
		if (!application.getOwner().equals(owner)) {
			Response.status(Status.FORBIDDEN).entity("Access denied.").build();
		}
		if (application.getAppStatus().equals(appStatus)) {
			Response.status(Status.NOT_MODIFIED)
					.entity("Application has not been modified.").build();
		}
		Query query = new Query();
		query.addCriteria(where("appid").is(appId))
			 .addCriteria(where("owner").is(owner))
			 .addCriteria(where("app_status").is(appStatus));
		mongoOps.updateFirst(query, update("app_status", appStatus),
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
		if (uid == null || appId == null) {
			Response.status(Status.BAD_REQUEST)
					.entity("Request must contains uid and appid param")
					.build();
		}
		if (!checkUserExist(uid)) {
			Response.status(Status.NOT_FOUND).entity("No such user.").build();
		}
		if (!checkApplicationExist(appId)) {
			Response.status(Status.NOT_FOUND).entity("No such application.").build();
		}
		Query query = new Query();
		query.addCriteria(where("uid").is(uid))
			 .addCriteria(where("appid").is(appId));
		mongoOps.remove(query, Collections.TOKEN_COLLECTION_NAME);
		return Response.noContent().build();
	}

	@Override
	@DELETE
	@Path("delete/{appid}")
	public Response delete(@PathParam("appid") String appId, 
			@QueryParam("owner") String owner) {
		if (owner == null) {
			Response.status(Status.BAD_REQUEST)
					.entity("Request must contains owner param")
					.build();
		}
		if (!checkUserExist(owner)) {
			Response.status(Status.NOT_FOUND).entity("No such user.").build();
		}
		Application application = mongoOps.findOne(
				query(where("appid").is(appId)), Application.class,
				Collections.APPLICATION_COLLECTION_NAME);
		if (application == null) {
			Response.status(Status.NOT_FOUND).entity("No such application.").build();
		}
		if (!application.getOwner().equals(owner)) {
			Response.status(Status.FORBIDDEN).entity("Access denied.").build();
		}
		cancelAllAuthorization(appId);
		Query query = new Query();
		query.addCriteria(where("appid").is(appId))
			 .addCriteria(where("owner").is(owner));
		mongoOps.remove(query, Collections.APPLICATION_COLLECTION_NAME);
		return Response.noContent().build();
	}
	
	private boolean checkApplicationExist(String appId) {
		Application app = mongoOps.findOne(query(where("appid").is(appId)),
				Application.class, Collections.APPLICATION_COLLECTION_NAME);
		return app == null;
	}
	
	private boolean checkUserExist(String uid) {
		Account account = mongoOps.findOne(query(where(Collections.Account.UID)
				.is(uid)), Account.class, Collections.ACCOUNT_COLLECTION_NAME);
		return account == null;
	}
	
	private boolean checkOwner(String appId, String owner) {
		Query query = new Query();
		query.addCriteria(where("appid").is(appId));
		query.addCriteria(where("owner").is(owner));
		Application app = mongoOps.findOne(query, Application.class,
				Collections.APPLICATION_COLLECTION_NAME);
		return app == null;
	}
	
	private void cancelAllAuthorization(String appId) {
		mongoOps.remove(query(where("appid").is(appId)), Collections.TOKEN_COLLECTION_NAME);
	}

}
