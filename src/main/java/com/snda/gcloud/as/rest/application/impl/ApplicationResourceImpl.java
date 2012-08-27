package com.snda.gcloud.as.rest.application.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

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
import org.springframework.stereotype.Service;

import com.snda.gcloud.as.mongo.model.Account;
import com.snda.gcloud.as.mongo.model.Application;
import com.snda.gcloud.as.mongo.model.Collections;
import com.snda.gcloud.as.mongo.model.Token;
import com.snda.gcloud.as.rest.application.ApplicationResource;
import com.snda.gcloud.as.rest.util.ApplicationKeys;
import com.snda.gcloud.as.rest.util.ObjectMappers;


@Service
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
		if (checkApplicationExist(appId)) {
			Response.status(Status.CONFLICT).entity("Application already exists.").build();
		}
		if (uid == null || appDescription == null || appStatus == null) {
			Response.status(Status.BAD_REQUEST)
					.entity("Request must contains uid, app_description and app_status params")
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
		if (!checkUserExist(owner)) {
			Response.status(Status.NOT_FOUND).entity("No such user.").build();
		}
		Application application = mongoOps.findOne(query(where("appid").is(appId)), Application.class, Collections.APPLICATION_COLLECTION_NAME);
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
			@QueryParam("uid") String uid, 
			@QueryParam("app_description") String appDescription,
			@QueryParam("website") String website) {
		return null;
	}

	@Override
	@POST
	@Path("status/{appid}")
	public Response changeStatus(@PathParam("appid") String appId, 
			@QueryParam("uid") String uid, 
			@QueryParam("app_status") String appStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@DELETE
	@Path("token")
	public Response cancelAuthorization(@QueryParam("uid") String uid, 
			@QueryParam("appid") String appId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@DELETE
	@Path("delete/{appid}")
	public Response delete(@PathParam("appid") String appId, 
			@QueryParam("uid") String uid) {
		// TODO Auto-generated method stub
		return null;
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

}
