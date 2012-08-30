package com.snda.grand.space.as.rest.account.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

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
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.snda.grand.space.as.mongo.model.Account;
import com.snda.grand.space.as.mongo.model.Application;
import com.snda.grand.space.as.mongo.model.Collections;
import com.snda.grand.space.as.rest.account.AccountResource;
import com.snda.grand.space.as.rest.util.ApplicationKeys;
import com.snda.grand.space.as.rest.util.ObjectMappers;

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
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Request must contains sndaId and display_name params.")
					.build();
		}
		String uid = ApplicationKeys.generateAccessKeyId();
		if (isBlank(locale)) {
			locale = "zh_CN";
		}
		long creationTime = System.currentTimeMillis();
		Account account = new Account(sndaId, uid, displayName, email, locale,
				creationTime, creationTime, true);
		mongoOps.insert(account, Collections.ACCOUNT_COLLECTION_NAME);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, account.getModelAccount()))
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
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Request must contains sndaId param.")
					.build();
		}
		Account account = mongoOps.findOne(query(where(Collections.Account.SNDA_ID).is(sndaId)),
				Account.class, Collections.ACCOUNT_COLLECTION_NAME);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Account : {}", account);
		}
		if (account == null) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such account.")
					.build();
		}
		if (isBlank(displayName) && isBlank(email) && isBlank(locale)) {
			return Response
					.status(Status.NOT_MODIFIED)
					.entity("Account has not been changed.")
					.build();
		}

		long modifiedTime = System.currentTimeMillis();
		Update update = new Update();
		String modifiedDisplayName = isBlank(displayName) ? account.getDisplayName() : displayName;
		String modifiedEmail = isBlank(email) ? account.getEmail() : email;
		String modifiedLocale = isBlank(locale) ? account.getLocale() : locale;
		update.set(Collections.Account.DISPLAY_NAME, modifiedDisplayName)
			  .set(Collections.Account.EMAIL, modifiedEmail)
			  .set(Collections.Account.LOCALE, modifiedLocale)
			  .set(Collections.Account.MODIFIED_TIME, modifiedTime);
		account.setDisplayName(modifiedDisplayName)
			   .setEmail(modifiedEmail)
			   .setLocale(modifiedLocale)
			   .setCreationTime(modifiedTime);
		mongoOps.updateFirst(query(where(Collections.Account.SNDA_ID)
				.is(sndaId)), update, Collections.ACCOUNT_COLLECTION_NAME);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, account.getModelAccount()))
				.build();
	}

	@Override
	@POST
	@Path("available/{snda_id}")
	public Response available(@PathParam("snda_id") String sndaId,
			@QueryParam("available") String available) {
		if (isBlank(sndaId) || isBlank(available)) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Request must contains sndaId and available params.")
					.build();
		}
		if (!checkAccountExist(sndaId)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such account.")
					.build();
		}
		long modifiedTime = System.currentTimeMillis();
		boolean enable = Boolean.parseBoolean(available);
		Update update = new Update();
		update.set(Collections.Account.AVAILABLE, enable)
			  .set(Collections.Account.MODIFIED_TIME, modifiedTime);
		mongoOps.updateFirst(query(where(Collections.Account.SNDA_ID)
				.is(sndaId)), update, Collections.ACCOUNT_COLLECTION_NAME);
		Account account = mongoOps.findOne(
				query(where(Collections.Account.SNDA_ID).is(sndaId)),
				Account.class, Collections.ACCOUNT_COLLECTION_NAME);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, account.getModelAccount()))
				.build();
	}

	@Override
	@GET
	@Path("status/{snda_id}")
	public Response status(@PathParam("snda_id") String sndaId) {
		if (isBlank(sndaId)) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Request must contains sndaId param.")
					.build();
		}
		Account account = mongoOps.findOne(
				query(where(Collections.Account.SNDA_ID).is(sndaId)),
				Account.class, Collections.ACCOUNT_COLLECTION_NAME);
		if (account == null) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such account.")
					.build();
		}
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, account.getModelAccount()))
				.build();
	}

	@Override
	@GET
	@Path("applications/{snda_id}")
	public Response applications(@PathParam("snda_id") String sndaId) {
		if (isBlank(sndaId)) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Request must contains sndaId param.")
					.build();
		}
		Account account = mongoOps.findOne(
				query(where(Collections.Account.SNDA_ID).is(sndaId)),
				Account.class, Collections.ACCOUNT_COLLECTION_NAME);
		if (account == null) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such account.")
					.build();
		}
		List<Application> apps = mongoOps.find(
				query(where(Collections.Application.OWNER).is(account.getUid())),
				Application.class, Collections.APPLICATION_COLLECTION_NAME);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, Application.getModelApplications(apps)))
				.build();
	}
	
	private boolean checkAccountExist(String sndaId) {
		Account account = mongoOps.findOne(
				query(where(Collections.Account.SNDA_ID).is(sndaId)),
				Account.class, Collections.ACCOUNT_COLLECTION_NAME);
		return account != null;
	}
	
}
