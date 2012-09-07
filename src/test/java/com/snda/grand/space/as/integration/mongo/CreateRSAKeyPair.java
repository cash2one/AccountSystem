package com.snda.grand.space.as.integration.mongo;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.net.UnknownHostException;
import java.util.UUID;
import java.util.regex.Matcher;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.Mongo;
import com.snda.grand.space.as.exception.InvalidAppStatusException;
import com.snda.grand.space.as.mongo.model.Collections;
import com.snda.grand.space.as.mongo.model.PojoAuthorization;
import com.snda.grand.space.as.rest.util.ApplicationKeys;
import com.snda.grand.space.as.rest.util.Preconditions;
import com.snda.grand.space.as.rest.util.Rule;

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
		System.out.println(Boolean.parseBoolean("1123"));
	}
	
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
	
	@Test
	public void testCheckEmail() {
		System.out.println(Rule.checkEmail("jian.g123@1.com"));
	}
	
	@Test
	public void testCheckDomain() {
		System.out.println(Rule.checkDomain("account.grandmobile.cn:8080/abc/acb"));
	}
	
	@Test
	public void testCheckSubDomain() {
		Preconditions.checkSubDomain("account.grandmobile.cn", "account.grandmobile.cn/abc/acb");
	}
	
	@Test
	public void testCheckStatus() {
		String appStatus = "development";
		if (appStatus == null 
				|| (!"development".equalsIgnoreCase(appStatus)
						&& !"release".equalsIgnoreCase(appStatus))) {
			throw new InvalidAppStatusException();
		}
	}
	
	@Test
	public void testCreateAuthorization() throws UnknownHostException {
		MongoOperations mongoOps = new MongoTemplate(new Mongo("account.grandmobile.cn", 27017), "account-system");
		String uid = "3MR0WHSZKGSI6B908O2D191N5";
		String appId = "poker";
		String refreshToken = "689c196fc988439e326c8fe33befaf0";
		PojoAuthorization auth = new PojoAuthorization(uid, appId, refreshToken, System.currentTimeMillis());
		mongoOps.insert(auth, Collections.AUTHORIZATION_COLLECTION_NAME);
	}
	
	@Test
	public void testCheckSignature() {
		Preconditions.basicAuthorizationValidate("Basic WXpFeU16YzJNbVV0TkdaaFppMDBNVFkzTFRobVpXRXRZbU16WVRkbE4yVTROV1kzOnNlc2FtIG9wZW4=", "E6N84MWP5JJ2392QEEOMQK3UT", "YzEyMzc2MmUtNGZhZi00MTY3LThmZWEtYmMzYTdlN2U4NWY3");
	}
	
	@Test
	public void test() {
		System.out.println(Base64.encodeBase64String("E6N84MWP5JJ2392QEEOMQK3UT:YzEyMzc2MmUtNGZhZi00MTY3LThmZWEtYmMzYTdlN2U4NWY3".getBytes()));
	}
	
}
