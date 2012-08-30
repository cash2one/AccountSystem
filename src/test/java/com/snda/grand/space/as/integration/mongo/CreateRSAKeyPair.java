package com.snda.grand.space.as.integration.mongo;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import com.snda.grand.space.as.rest.util.ApplicationKeys;

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
	
//	@Test
//	public void mongoTest() throws UnknownHostException, MongoException {
//		MongoOperations mongoOps = new MongoTemplate(new Mongo(), "account-system");
//		Account account = mongoOps.findOne(query(where(Collections.Account.UID)
//				.is("BESY002INT9SBBAVE6110SBGH")), Account.class,
//				Collections.ACCOUNT_COLLECTION_NAME);
//		System.out.println(account);
//	}
//	
//	@Test
//	public void springTest() {
//		ApplicationContext ctx = new FileSystemXmlApplicationContext(
//				new String[] { "src/main/resources/account.system.mongo.xml",
//						"src/main/resources/account.system.account.rest.xml" });
//		MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
//		Account account = mongoOps.findOne(query(where(Collections.Account.SNDA_ID).is("sndaid001")),
//				Account.class, Collections.ACCOUNT_COLLECTION_NAME);
//		System.out.println(account);
//	}
	
}
