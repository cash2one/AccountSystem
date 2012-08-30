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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.snda.grand.space.as.exception.InvalidDisplayNameException;
import com.snda.grand.space.as.exception.InvalidSndaIdException;
import com.snda.grand.space.as.mongo.model.PojoAccount;
import com.snda.grand.space.as.mongo.model.PojoApplication;
import com.snda.grand.space.as.mongo.model.Collections;
import com.snda.grand.space.as.rest.account.AccountResource;
import com.snda.grand.space.as.rest.model.Account;
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
	@Produces(MediaType.APPLICATION_JSON)
	public Account create(@PathParam("snda_id") String sndaId,
			@QueryParam("display_name") String displayName,
			@QueryParam("email") String email,
			@QueryParam("locale") String locale) {
		checkSndaId(sndaId);
		checkDisplayName(displayName);
		checkEmail(email);
		String uid = ApplicationKeys.generateAccessKeyId();
		if (isBlank(locale)) {
			locale = "zh_CN";
		}
		long creationTime = System.currentTimeMillis();
		PojoAccount account = new PojoAccount(sndaId, uid, displayName, email, locale,
				creationTime, creationTime, true);
		mongoOps.insert(account, Collections.ACCOUNT_COLLECTION_NAME);
		return account.getAccount();
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
		PojoAccount account = mongoOps.findOne(query(where(Collections.Account.SNDA_ID).is(sndaId)),
				PojoAccount.class, Collections.ACCOUNT_COLLECTION_NAME);
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
				.entity(ObjectMappers.toJSON(MAPPER, account.getAccount()))
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
		PojoAccount account = mongoOps.findOne(
				query(where(Collections.Account.SNDA_ID).is(sndaId)),
				PojoAccount.class, Collections.ACCOUNT_COLLECTION_NAME);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, account.getAccount()))
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
		PojoAccount account = mongoOps.findOne(
				query(where(Collections.Account.SNDA_ID).is(sndaId)),
				PojoAccount.class, Collections.ACCOUNT_COLLECTION_NAME);
		if (account == null) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such account.")
					.build();
		}
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, account.getAccount()))
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
		PojoAccount account = mongoOps.findOne(
				query(where(Collections.Account.SNDA_ID).is(sndaId)),
				PojoAccount.class, Collections.ACCOUNT_COLLECTION_NAME);
		if (account == null) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("No such account.")
					.build();
		}
		List<PojoApplication> apps = mongoOps.find(
				query(where(Collections.Application.OWNER).is(account.getUid())),
				PojoApplication.class, Collections.APPLICATION_COLLECTION_NAME);
		return Response
				.ok()
				.entity(ObjectMappers.toJSON(MAPPER, PojoApplication.getApplications(apps)))
				.build();
	}
	
	private boolean checkAccountExist(String sndaId) {
		PojoAccount account = mongoOps.findOne(
				query(where(Collections.Account.SNDA_ID).is(sndaId)),
				PojoAccount.class, Collections.ACCOUNT_COLLECTION_NAME);
		return account != null;
	}
	
	private void checkSndaId(String sndaId) {
		if (isBlank(sndaId)) {
			throw new InvalidSndaIdException();
		}
	}
	
	private void checkDisplayName(String displayName) {
		if (isBlank(displayName)) {
			throw new InvalidDisplayNameException();
		}
	}
	
	private void checkEmail(String email) {
		// TODO Auto-generated method stub
		
	}
	
}
