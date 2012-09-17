package com.snda.grand.space.as.integration.mongo;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.amber.oauth2.as.issuer.MD5Generator;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.Mongo;
import com.snda.grand.space.as.exception.InvalidAppStatusException;
import com.snda.grand.space.as.mongo.internal.model.Accessor;
import com.snda.grand.space.as.mongo.model.MongoCollections;
import com.snda.grand.space.as.mongo.model.PojoAccount;
import com.snda.grand.space.as.mongo.model.PojoApplication;
import com.snda.grand.space.as.rest.util.ApplicationKeys;
import com.snda.grand.space.as.rest.util.Constants;
import com.snda.grand.space.as.rest.util.Preconditions;
import com.snda.grand.space.as.rest.util.Rule;
import com.snda.grand.space.as.util.MD5;

public class CreateRSAKeyPair {

	@Test
	public void createAccount() throws Exception {
		long creationTime = System.currentTimeMillis();
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new Mongo(
				"account.grandmobile.cn", 27017), "account-system");
		MongoOperations mongoOps = new MongoTemplate(mongoDbFactory);
		PojoAccount pojoAccount = new PojoAccount("superadmin", "0",
				"superadmin", "superadmin", null, null, creationTime,
				creationTime, true);
		mongoOps.insert(pojoAccount, MongoCollections.ACCOUNT_COLLECTION_NAME);
	}
	
	@Test
	public void createWWWApplication() throws Exception {
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new Mongo(
				"account.grandmobile.cn", 27017), "account-system");
		MongoOperations mongoOps = new MongoTemplate(mongoDbFactory);
		MD5Generator md5gen = new MD5Generator();
		long creationTime = System.currentTimeMillis();
		PojoApplication pojoApplicaiton = new PojoApplication("www", "www", "release",
				md5gen.generateValue(), md5gen.generateValue(), "SNDA", "global", null,
				creationTime, creationTime, "0");
		mongoOps.insert(pojoApplicaiton, MongoCollections.APPLICATION_COLLECTION_NAME);
	}
	
	@Test
	public void createApplication() throws Exception {
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new Mongo(
				"account.grandmobile.cn", 27017), "account-system");
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
				"account.grandmobile.cn", 27017), "account-test");
		MongoOperations mongoOps = new MongoTemplate(mongoDbFactory);
		
		MD5Generator md5gen = new MD5Generator();
		Accessor testAccessor = new Accessor("test",
				md5gen.generateValue(),
				md5gen.generateValue(),
				"For test");
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
						"src/main/resources/account.system.oauth2.rest.xml"});
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
	
//	@Test
//	public void testCreateAuthorization() throws UnknownHostException {
//		MongoOperations mongoOps = new MongoTemplate(new Mongo("account.grandmobile.cn", 27017), "account-system");
//		String uid = "3MR0WHSZKGSI6B908O2D191N5";
//		String appId = "poker";
//		String refreshToken = "689c196fc988439e326c8fe33befaf0";
//		PojoAuthorization auth = new PojoAuthorization(uid, appId, refreshToken, System.currentTimeMillis());
//		mongoOps.insert(auth, MongoCollections.AUTHORIZATION_COLLECTION_NAME);
//	}
	
	@Test
	public void testCheckSignature() {
		Preconditions.basicAuthorizationValidate("Basic WXpFeU16YzJNbVV0TkdaaFppMDBNVFkzTFRobVpXRXRZbU16WVRkbE4yVTROV1kzOnNlc2FtIG9wZW4=", "E6N84MWP5JJ2392QEEOMQK3UT", "YzEyMzc2MmUtNGZhZi00MTY3LThmZWEtYmMzYTdlN2U4NWY3");
	}
	
	@Test
	public void test() {
		System.out.println(Base64.encodeBase64String("E6N84MWP5JJ2392QEEOMQK3UT:YzEyMzc2MmUtNGZhZi00MTY3LThmZWEtYmMzYTdlN2U4NWY3".getBytes()));
	}
	
	@Test
	public void testGetQueryFromURI() throws Exception {
		List<String> queryList = Preconditions.getQueriesFromQueryString("bvara=1&varb=2&123%3D123&123=");
		for (String query : queryList) {
			System.out.println(query);
		}
		Collections.sort(queryList);
		System.out.println("===========================");
		for (String query : queryList) {
			System.out.println(query);
		}
	}
	
	@Test
	public void testMD5() {
		String str = "appId=917areaId=1customSecurityLevel=1merchant_name=1_917_702signature_method=MD5ticket=ULS5033781170ed4df0bde6dcbad32bce88timestamp=1347332443za1}5DE=wyILupWX"; 
		System.out.println(MD5.hexDigest(str.getBytes()));
	}
	
//	@Test
//	public void testMMMM() {
//		String str = "appId=917&areaId=1&customSecurityLevel=1&merchant_name=1_917_702&signature_method=MD5&ticket=ULS5033781170ed4df0bde6dcbad32bce88&timestamp=1347332443";
//		List<String> list = Preconditions.getQueriesFromQueryString(str);
//		list = Preconditions.getSdoValidateCanonicalQueryList(list);
//		for (String item : list) {
//			System.out.println(item);
//		}
//		String shit = Preconditions.makeSignedSdoValidateUrl(list);
//		System.out.println(shit);
//	}
	
}
