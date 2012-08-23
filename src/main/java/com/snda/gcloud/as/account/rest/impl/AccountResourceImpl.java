package com.snda.gcloud.as.account.rest.impl;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.UUID;

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
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.stereotype.Service;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.snda.gcloud.as.account.rest.AccountResource;
import com.snda.gcloud.as.mongo.model.Account;
import com.snda.gcloud.as.rest.util.Constants;
import com.snda.gcloud.as.rest.util.ObjectMappers;


@Service
@Path("/")
public class AccountResourceImpl implements AccountResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountResourceImpl.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static MongoOperations mongoOps;
	
	public AccountResourceImpl(/*Mongo mongo*/) {
		LOGGER.info("AccountResourceImpl initialized.");
		try {
			mongoOps = new MongoTemplate(new SimpleMongoDbFactory(new Mongo(), "account-system"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}

	@Override
	@POST
	@Path("create/{snda_id}")
	public Response create(@PathParam("snda_id") String sndaId,
			@QueryParam("display_name") String displayName, 
			@QueryParam("email") String email, 
			@QueryParam("locale") String locale) {
		if (displayName == null) {
			return Response.status(Status.BAD_REQUEST)
					.entity("You did not provide the username param in path.")
					.build();
		}
		String uid = UUID.randomUUID().toString();
		if (locale == null) {
			locale = "zh_CN";
		}
		Account account = new Account(sndaId, uid, displayName, email,
				locale, System.currentTimeMillis(),
				new ArrayList(), true);
		mongoOps.insert(account, Constants.ACCOUNT_COLLECTION_NAME);
		return Response.ok().entity(ObjectMappers.toJSON(MAPPER, account.getModelAccount())).build();
	}

	@Override
	public Response modify() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response disable() {
		// TODO Auto-generated method stub
		return null;
	}

}
