package com.snda.gcloud.as.rest.account.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.snda.gcloud.as.mongo.model.Account;
import com.snda.gcloud.as.mongo.model.Application;
import com.snda.gcloud.as.mongo.model.Collections;
import com.snda.gcloud.as.rest.account.AccountResource;
import com.snda.gcloud.as.rest.util.ApplicationKeys;
import com.snda.gcloud.as.rest.util.Constants;
import com.snda.gcloud.as.rest.util.ObjectMappers;

@Service
@Path("account")
public class AccountResourceImpl implements AccountResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AccountResourceImpl.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static MongoOperations mongoOps;

	public AccountResourceImpl(MongoOperations mongoOperations) {
		checkNotNull(mongoOperations, "MongoTemplate is null.");
		LOGGER.info("AccountResourceImpl initialized.");
		mongoOps = mongoOperations;
	}

	@Override
	@POST
	@Path("create/{snda_id}")
	public Response create(@PathParam("snda_id") String sndaId,
			@QueryParam("display_name") String displayName,
			@QueryParam("email") String email,
			@QueryParam("locale") String locale) {
		if (isBlank(sndaId) || isBlank(displayName)) {
			return Response.status(Status.BAD_REQUEST)
					.entity("Request must contains sndaId and display_name params.")
					.build();
		}
		String uid = ApplicationKeys.generateAccessKeyId();
		if (locale == null) {
			locale = "zh_CN";
		}
		Account account = new Account(sndaId, uid, displayName, email, locale,
				System.currentTimeMillis(), true);
		mongoOps.insert(account, Constants.ACCOUNT_COLLECTION_NAME);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, getModelAccount(account)))
				.build();
	}

	@Override
	@POST
	@Path("modify/{snda_id}")
	public Response modify(@PathParam("snda_id") String sndaId,
			@QueryParam("display_name") String displayName,
			@QueryParam("email") String email,
			@QueryParam("locale") String locale) {
		if (isBlank(sndaId)) {
			return Response.status(Status.BAD_REQUEST)
					.entity("Request must contains sndaId param.")
					.build();
		}
		Account account = mongoOps.findOne(query(where("snda_id").is(sndaId)),
				Account.class, Constants.ACCOUNT_COLLECTION_NAME);
		if (account == null) {
			return Response.status(Status.NOT_FOUND).entity("No such account.")
					.build();
		}
		if (account.getDisplayName().equals(displayName)
				&& account.getEmail().equalsIgnoreCase(email)
				&& account.getLocale().equals(locale)) {
			return Response.status(Status.NOT_MODIFIED)
					.entity("Account has not been changed.").build();
		}

		Update update = new Update();
		update.set("display_name", displayName).set("email", email)
				.set("locale", locale);
		account.setDisplayName(displayName);
		account.setEmail(email);
		account.setLocale(locale);
		mongoOps.updateFirst(query(where("snda_id").is(sndaId)), update,
				Constants.ACCOUNT_COLLECTION_NAME);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, getModelAccount(account)))
				.build();
	}

	@Override
	@POST
	@Path("available/{snda_id}")
	public Response available(@PathParam("snda_id") String sndaId,
			@QueryParam("available") String available) {
		if (isBlank(sndaId) || isBlank(available)) {
			return Response.status(Status.BAD_REQUEST)
					.entity("Request must contains sndaId and available params.")
					.build();
		}
		if (!checkAccountExist(sndaId)) {
			return Response.status(Status.NOT_FOUND).entity("No such account.")
					.build();
		}
		boolean enable = Boolean.parseBoolean(available);
		mongoOps.updateFirst(query(where("snda_id").is(sndaId)),
				update("available", enable), Constants.ACCOUNT_COLLECTION_NAME);
		Account account = mongoOps.findOne(query(where("snda_id").is(sndaId)),
				Account.class, Constants.ACCOUNT_COLLECTION_NAME);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, getModelAccount(account)))
				.build();
	}

	@Override
	@GET
	@Path("status/{snda_id}")
	public Response status(@PathParam("snda_id") String sndaId) {
		if (isBlank(sndaId)) {
			return Response.status(Status.BAD_REQUEST)
					.entity("Request must contains sndaId param.")
					.build();
		}
		Account account = mongoOps.findOne(query(where("snda_id").is(sndaId)),
				Account.class, Constants.ACCOUNT_COLLECTION_NAME);
		if (account == null) {
			return Response.status(Status.NOT_FOUND).entity("No such account.")
					.build();
		}
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, getModelAccount(account)))
				.build();
	}

	@Override
	@GET
	@Path("applications/{snda_id}")
	public Response applications(@PathParam("snda_id") String sndaId) {
		if (isBlank(sndaId)) {
			return Response.status(Status.BAD_REQUEST)
					.entity("Request must contains sndaId param.")
					.build();
		}
		if (!checkAccountExist(sndaId)) {
			return Response.status(Status.NOT_FOUND).entity("No such account.")
					.build();
		}
		List<Application> apps = mongoOps.find(
				query(where(Collections.Application.OWNER).is(sndaId)),
				Application.class, Collections.APPLICATION_COLLECTION_NAME);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, Application.getModelApplications(apps)))
				.build();
	}
	
	private boolean checkAccountExist(String sndaId) {
		Account account = mongoOps.findOne(query(where("snda_id").is(sndaId)),
				Account.class, Constants.ACCOUNT_COLLECTION_NAME);
		return account == null;
	}
	
	public com.snda.gcloud.as.rest.model.Account getModelAccount(Account fromAccount) {
		com.snda.gcloud.as.rest.model.Account account = new com.snda.gcloud.as.rest.model.Account();
		account.setUid(fromAccount.getUid());
		account.setDisplay_name(fromAccount.getDisplayName());
		account.setEmail(fromAccount.getEmail());
		account.setLocale(fromAccount.getLocale());
		account.setCreationTime(new DateTime(fromAccount.getCreationTime()));
		account.setAvailable(fromAccount.isAvailable());
		return account;
	}

}
