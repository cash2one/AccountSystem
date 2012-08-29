package com.snda.gcloud.as.integration.mongo;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.net.UnknownHostException;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.snda.gcloud.as.mongo.model.Account;
import com.snda.gcloud.as.mongo.model.Collections;
import com.snda.gcloud.as.rest.util.ApplicationKeys;
import com.snda.gcloud.as.rest.util.Constants;

public class CreateRSAKeyPair {

	@Test
	public void testCreateRSAKeyPair() {
		
	}
	
	@Test
	public void testIsBlank() {
		String a = "a";
		System.out.println(isBlank(null));
		System.out.println(Base64.encodeBase64String(UUID.randomUUID().toString().getBytes()));
		System.out.println(ApplicationKeys.generateAccessKeyId());
	}
	
	@Test
	public void mongoGenTest() throws UnknownHostException, MongoException {
		MongoOperations mongoOps = new MongoTemplate(new Mongo(), "account-system");
		String uid = ApplicationKeys.generateAccessKeyId();
		Account account = new Account("sndaid002", uid, "fuck12", "fuck12@gmail.com", "zh_CN",
				System.currentTimeMillis(), true);
		mongoOps.insert(account, Constants.ACCOUNT_COLLECTION_NAME);
	}

	@Test
	public void mongoTest() throws UnknownHostException, MongoException {
		MongoOperations mongoOps = new MongoTemplate(new Mongo(), "account-system");
		Account account = mongoOps.findOne(query(where(Collections.Account.UID)
				.is("BESY002INT9SBBAVE6110SBGH")), Account.class,
				Collections.ACCOUNT_COLLECTION_NAME);
		System.out.println(account);
	}
	
	@Test
	public void springTest() {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				new String[] { "src/main/resources/account.system.mongo.xml",
						"src/main/resources/account.system.account.rest.xml" });
		MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
		Account account = mongoOps.findOne(query(where(Collections.Account.SNDA_ID).is("sndaid001")),
				Account.class, Collections.ACCOUNT_COLLECTION_NAME);
		System.out.println(account);
	}
	
}
