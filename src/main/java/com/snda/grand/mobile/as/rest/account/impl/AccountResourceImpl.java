package com.snda.grand.mobile.as.rest.account.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkAvailableParam;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkDisplayName;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkEmail;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkLocale;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkNotModified;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkSndaId;
import static com.snda.grand.mobile.as.rest.util.Preconditions.checkUsernameNorm;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.snda.grand.mobile.as.processor.AccountResourceProcessor;
import com.snda.grand.mobile.as.rest.account.AccountResource;
import com.snda.grand.mobile.as.rest.model.Account;
import com.snda.grand.mobile.as.rest.model.Application;
import com.snda.grand.mobile.as.rest.model.Authorization;

@Service
@Path("account")
public class AccountResourceImpl implements AccountResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountResourceImpl.class);
	
	private final AccountResourceProcessor accountResourceProcessor;
	
	public AccountResourceImpl(AccountResourceProcessor accountResourceProcessor) {
		checkNotNull(accountResourceProcessor, "AccountResourceProcessor is null.");
		this.accountResourceProcessor = accountResourceProcessor;
		LOGGER.info("AccountResourceImpl initialized.");
	}

	@Override
	@POST
	@Path("create/{snda_id}")
	public Account create(@PathParam("snda_id") String sndaId,
			@QueryParam("username_norm") String usernameNorm,
			@QueryParam("display_name") String displayName,
			@QueryParam("email") String email,
			@QueryParam("locale") String locale) {
		checkSndaId(sndaId);
		checkDisplayName(displayName);
		checkUsernameNorm(usernameNorm);
		checkEmail(email);
		locale = checkLocale(locale);
		return accountResourceProcessor.create(sndaId, usernameNorm, displayName, email,
				locale, true);
	}

	@Override
	@POST
	@Path("modify/{snda_id}")
	public Account modify(@PathParam("snda_id") String sndaId,
			@QueryParam("display_name") String displayName,
			@QueryParam("email") String email,
			@QueryParam("locale") String locale) {
		checkSndaId(sndaId);
		checkNotModified(displayName, email, locale);
		if (email != null) {
			checkEmail(email);
		}
		return accountResourceProcessor.modify(sndaId, displayName, email,
				locale);
	}

	@Override
	@POST
	@Path("available/{snda_id}")
	public Account available(@PathParam("snda_id") String sndaId,
			@QueryParam("available") String available) {
		checkSndaId(sndaId);
		boolean enable = checkAvailableParam(available);
		return accountResourceProcessor.available(sndaId, enable);
	}

	@Override
	@GET
	@Path("status/{snda_id}")
	public Account status(@PathParam("snda_id") String sndaId) {
		checkSndaId(sndaId);
		return accountResourceProcessor.status(sndaId);
	}

	@Override
	@GET
	@Path("applications/{snda_id}")
	public List<Application> applications(@PathParam("snda_id") String sndaId) {
		checkSndaId(sndaId);
		return accountResourceProcessor.applications(sndaId);
	}
	
	@Override
	@GET
	@Path("authorizations/{snda_id}")
	public List<Authorization> listAuthorizations(@PathParam("snda_id") String sndaId) {
		checkSndaId(sndaId);
		return accountResourceProcessor.listAuthorizations(sndaId);
	}
	
}
