package com.snda.grand.space.as.account.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;

import com.snda.grand.space.as.account.AccountService;
import com.snda.grand.space.as.mongo.model.MongoCollections;
import com.snda.grand.space.as.mongo.model.PojoAccount;
import com.snda.grand.space.as.rest.model.Account;

public class MongoAccountService implements AccountService {
	
	private final MongoOperations mongoOps;
	
	public MongoAccountService(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
	}

	@Override
	public Account putAccount(String sndaId, String uid, String usernameNorm,
			String displayName, String email, String locale, boolean available,
			long creationTime, long modifiedTime) {
		PojoAccount account = new PojoAccount(sndaId, uid, usernameNorm, displayName,
				email, locale, creationTime, creationTime, true);
		mongoOps.insert(account, MongoCollections.ACCOUNT_COLLECTION_NAME);
		return newAccount(account);
	}
	
	@Override
	public Account updateAccount(String sndaId, String uid,
			String usernameNorm, String displayName, String email,
			String locale, boolean available, long creationTime,
			long modifiedTime) {
		Update update = new Update()
							.set(MongoCollections.Account.UID, uid)
							.set(MongoCollections.Account.USERNAME_NORM, usernameNorm)
							.set(MongoCollections.Account.DISPLAY_NAME, displayName)
							.set(MongoCollections.Account.EMAIL, email)
							.set(MongoCollections.Account.LOCALE, locale)
							.set(MongoCollections.Account.AVAILABLE, available)
							.set(MongoCollections.Account.CREATION_TIME, creationTime)
							.set(MongoCollections.Account.MODIFIED_TIME, modifiedTime);
		mongoOps.updateFirst(
				query(where(MongoCollections.Account.SNDA_ID).is(sndaId)),
				update, MongoCollections.ACCOUNT_COLLECTION_NAME);
		return newAccount(sndaId, uid, usernameNorm, displayName, email, locale,
				available, creationTime, modifiedTime);
	}

	@Override
	public Account getAccountBySndaId(String sndaId) {
		PojoAccount account = mongoOps.findOne(
				query(where(MongoCollections.Account.SNDA_ID).is(sndaId)),
				PojoAccount.class, MongoCollections.ACCOUNT_COLLECTION_NAME);
		return newAccount(account);
	}
	
	@Override
	public Account getAccountByUid(String uid) {
		PojoAccount account = mongoOps.findOne(
				query(where(MongoCollections.Account.UID).is(uid)),
				PojoAccount.class, MongoCollections.ACCOUNT_COLLECTION_NAME);
		return newAccount(account);
	}
	
	@Override
	public void deleteAccountBySndaId(String sndaId) {
		mongoOps.remove(query(where(MongoCollections.Account.SNDA_ID)
				.is(sndaId)), MongoCollections.ACCOUNT_COLLECTION_NAME);
	}

	private Account newAccount(PojoAccount pojoAccount) {
		Account account = null;
		if (pojoAccount != null) {
			account = pojoAccount.getAccount();
		}
		return account;
	}
	
	private Account newAccount(String sndaId, String uid,
			String usernameNorm, String displayName, String email,
			String locale, boolean available, long creationTime,
			long modifiedTime) {
		Account account = new Account();
		account.setSndaId(sndaId);
		account.setUid(uid);
		account.setUsernameNorm(usernameNorm);
		account.setDisplayName(displayName);
		account.setEmail(email);
		account.setLocale(locale);
		account.setAvailable(available);
		account.setCreationTime(new DateTime(creationTime));
		account.setModifiedTime(new DateTime(modifiedTime));
		return account;
	}

}
