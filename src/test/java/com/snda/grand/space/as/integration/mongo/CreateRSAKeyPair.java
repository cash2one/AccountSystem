package com.snda.grand.space.as.integration.mongo;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.net.UnknownHostException;
import java.util.UUID;

import org.apache.amber.oauth2.as.issuer.MD5Generator;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.Mongo;
import com.snda.grand.mobile.as.exception.InvalidAppStatusException;
import com.snda.grand.mobile.as.mongo.internal.model.Accessor;
import com.snda.grand.mobile.as.mongo.model.MongoCollections;
import com.snda.grand.mobile.as.mongo.model.PojoAccount;
import com.snda.grand.mobile.as.mongo.model.PojoApplication;
import com.snda.grand.mobile.as.rest.util.ApplicationKeys;
import com.snda.grand.mobile.as.rest.util.Constants;
import com.snda.grand.mobile.as.rest.util.Preconditions;
import com.snda.grand.mobile.as.rest.util.Rule;
import com.snda.grand.mobile.as.util.InternalIpAllow;

public class CreateRSAKeyPair {
	
	private static final String MONGOIC_URL = "xxxxxxxxxxxxxx";
	private static final UserCredentials userCredentials = new UserCredentials("xxxxxxxxxx", "xxxxxxxxxxxxx");

	@Test
	public void createAccount() throws Exception {
		long creationTime = System.currentTimeMillis();
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new Mongo(
				MONGOIC_URL, 27017), "account-system", userCredentials);
		MongoOperations mongoOps = new MongoTemplate(mongoDbFactory);
		PojoAccount pojoAccount = new PojoAccount("superadmin", "0",
				"superadmin", "superadmin", null, null, creationTime,
				creationTime, true);
		mongoOps.insert(pojoAccount, MongoCollections.ACCOUNT_COLLECTION_NAME);
	}
	
	@Test
	public void createWWWApplication() throws Exception {
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new Mongo(
				MONGOIC_URL, 27017), "account-system", userCredentials);
		MongoOperations mongoOps = new MongoTemplate(mongoDbFactory);
		MD5Generator md5gen = new MD5Generator();
		long creationTime = System.currentTimeMillis();
		PojoApplication pojoApplicaiton = new PojoApplication("www", "www", "release",
				md5gen.generateValue(), md5gen.generateValue(), "SNDA", "full", null,
				creationTime, creationTime, "0");
		mongoOps.insert(pojoApplicaiton, MongoCollections.APPLICATION_COLLECTION_NAME);
	}
	
	@Test
	public void createApplication() throws Exception {
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new Mongo(
				MONGOIC_URL, 27017), "account-system", userCredentials);
		MongoOperations mongoOps = new MongoTemplate(mongoDbFactory);
		MD5Generator md5gen = new MD5Generator();
		long creationTime = System.currentTimeMillis();
		PojoApplication pojoApplicaiton = new PojoApplication("fullapp", "1231234123", "release",
				md5gen.generateValue(), md5gen.generateValue(), "test", "full", null,
				creationTime, creationTime, "4NHL6TJGU5V9HJYC7QVA63E9F");
		mongoOps.insert(pojoApplicaiton, MongoCollections.APPLICATION_COLLECTION_NAME);
	}
	
	@Test
	public void testGenAccessor() throws UnknownHostException, OAuthSystemException {
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new Mongo(
				MONGOIC_URL, 27017), "account-system", userCredentials);
		MongoOperations mongoOps = new MongoTemplate(mongoDbFactory);
		
		MD5Generator md5gen = new MD5Generator();
		Accessor testAccessor = new Accessor("accessor",
				md5gen.generateValue(),
				md5gen.generateValue(),
				"Raw Accessor");
		mongoOps.insert(testAccessor, MongoCollections.ACCESSOR_COLLECTION_NAME);
	}
	
	@Test
	public void testCreateRSAKeyPair() {
		String redirectUri = null;
		String result = (redirectUri == null ? Constants.DEFAULT_AUTHORIZE_SUCCESS_REDIRECT_URI : redirectUri);
		System.out.println(result);
	}
	
	@Test
	public void testIsBlank() {
		String a = "a";
		System.out.println(isBlank(null));
		System.out.println(Base64.encodeBase64String(UUID.randomUUID().toString().getBytes()));
		System.out.println(ApplicationKeys.generateAccessKeyId());
		System.out.println(Boolean.parseBoolean("1123"));
	}
	
	@Test
	public void springTest() {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				new String[] { "src/main/resources/account.system.mongo.xml",
						"src/main/resources/account.system.memcached.xml",
						"src/main/resources/account.system.account.rest.xml",
						"src/main/resources/account.system.processor.xml",
						"src/main/resources/account.system.oauth2.rest.xml",
						"src/main/resources/account.system.security.xml"});
	}
	
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
		Preconditions.checkSubDomain("account.grandmobile.cn", "http://account.grandmobile.cn/abc/acb");
		Preconditions.checkSubDomain("account.grandmobile.cn", "https://account.grandmobile.cn");
		Preconditions.checkSubDomain("account.grandmobile.cn", "http://book.grandmobile.cn");
		Preconditions.checkSubDomain("account.grandmobile.cn", "http://book.grandmobile.cn:8080");
		Preconditions.checkSubDomain("account.grandmobile.cn", "https://account.grandmobile.cn:8080/abc/acb");
	}
	
	@Test
	public void testCheckIP() {
		InternalIpAllow allows = new InternalIpAllow("127.0.0.1, 114.80.133.7");
		System.out.println(allows.contains("127.0.0.1"));
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
	
}
