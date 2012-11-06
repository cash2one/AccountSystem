package com.snda.grand.space.as.integration.exception;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.HttpHeaders;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.Mongo;
import com.snda.grand.mobile.as.mongo.model.MongoCollections;
import com.snda.grand.mobile.as.mongo.model.PojoAccount;
import com.snda.grand.mobile.as.rest.model.Account;
import com.snda.grand.mobile.as.rest.util.ObjectMappers;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public class AccountTest {

	private Client client;
	private final ObjectMapper mapper = new ObjectMapper();
	private final String DEFAULT_URI = "https://account.grandmobile.cn/";
	private final String AUTH = "xxx";
	
	private static MongoOperations mongoOps;
	
	private final String TEST_ACCOUNT_SNDA_ID = "test_snda_id";
	private final String TEST_ACCOUNT_UID = "test_uid";
	private final String TEST_ACCOUNT_USERNAME_NORM = "test_username_norm";
	private final String TEST_ACCOUNT_DISPLAY_NAME = "test_display_name";
	private final String TEST_ACCOUNT_EMAIL = "test@123.com";
	private final String TEST_ACCOUNT_LOCALE = "en_US";
	private final long TEST_ACCOUNT_CREATION_TIME = System.currentTimeMillis();
	private final long TEST_ACCOUNT_MODIFIED_TIME = TEST_ACCOUNT_CREATION_TIME + (new Random()).nextInt(60 * 1000);
	private final boolean TEST_ACCOUNT_AVAILABLE = true;
	
	@Before
	public void createClient() {
		TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
			@Override
		    public X509Certificate[] getAcceptedIssuers(){return null;}
		    @Override
		    public void checkClientTrusted(X509Certificate[] certs, String authType){}
		    @Override
		    public void checkServerTrusted(X509Certificate[] certs, String authType){}
		}};
		try {
		    SSLContext sc = SSLContext.getInstance("TLS");
		    sc.init(null, trustAllCerts, new SecureRandom());
		    ClientConfig config = new DefaultClientConfig();
			config.getProperties().put(
					HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
					new HTTPSProperties(new HostnameVerifier() {

						@Override
						public boolean verify(String hostname, SSLSession session) {
							return true;
						}
						
					}, sc));
		    client = Client.create(config);
		} catch (Exception e) {
		    ;
		}
	}
	
	@Before
	public void prepare() throws Exception {
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new Mongo(
				"58.215.185.148", 27017), "account-system", new UserCredentials("grandmobileadmin", "lELD3vvFRUwuQCza"));
		mongoOps = new MongoTemplate(mongoDbFactory);
	}
	
	private void createTestAccount() {
		PojoAccount pojoAccount = new PojoAccount(TEST_ACCOUNT_SNDA_ID, 
				TEST_ACCOUNT_UID, TEST_ACCOUNT_USERNAME_NORM,
				TEST_ACCOUNT_DISPLAY_NAME, TEST_ACCOUNT_EMAIL, 
				TEST_ACCOUNT_LOCALE, TEST_ACCOUNT_CREATION_TIME,
				TEST_ACCOUNT_MODIFIED_TIME, TEST_ACCOUNT_AVAILABLE);
		mongoOps.insert(pojoAccount, MongoCollections.ACCOUNT_COLLECTION_NAME);
	}
	
	@After
	public void destroy() {
		
	}
	
	private void deleteTestAccount() {
		mongoOps.remove(
				query(where(MongoCollections.Account.SNDA_ID).is(
						TEST_ACCOUNT_SNDA_ID)),
				MongoCollections.ACCOUNT_COLLECTION_NAME);
	}
	
	@After
	public void destroyClient() {
		client.destroy();
	}
	
	@Test
	public void test() {
		testModifyAccount();
		testChangeAvailable();
		testGetStatus();
	}
	
	private void testModifyAccount() {
		createTestAccount();
		WebResource r = client.resource(DEFAULT_URI + "api/account/modify/" + TEST_ACCOUNT_SNDA_ID);
		ClientResponse response = r.queryParam("display_name", "changed_display_name")
								   .queryParam("email", "changed_test@123.com")
								   .queryParam("locale", "zh_CN")
								   .header(HttpHeaders.AUTHORIZATION, "Basic " + AUTH)
								   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(200));
		
		Account account = getAccount(response.getEntity(String.class));
		assertThat(account.getDisplayName(), is("changed_display_name"));
		assertThat(account.getEmail(), is("changed_test@123.com"));
		assertThat(account.getLocale(), is("zh_CN"));
		deleteTestAccount();
	}
	
	private void testChangeAvailable() {
		createTestAccount();
		WebResource r = client.resource(DEFAULT_URI + "api/account/available/" + TEST_ACCOUNT_SNDA_ID);
		ClientResponse response = r.queryParam("available", "false")
								   .header(HttpHeaders.AUTHORIZATION, "Basic " + AUTH)
								   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(200));
		
		Account account = getAccount(response.getEntity(String.class));
		assertThat(account.isAvailable(), is(false));
		deleteTestAccount();
	}
	
	private void testGetStatus() {
		WebResource r = client.resource(DEFAULT_URI + "api/account/status/" + TEST_ACCOUNT_SNDA_ID);
		ClientResponse response = r.header(HttpHeaders.AUTHORIZATION, "Basic " + AUTH)
								   .get(ClientResponse.class);
		assertThat(response.getStatus(), is(200));
		
		Account account = getAccount(response.getEntity(String.class));
		assertThat(account.getUid(), is(TEST_ACCOUNT_UID));
		assertThat(account.getUsernameNorm(), is(TEST_ACCOUNT_USERNAME_NORM));
		assertThat(account.getDisplayName(), is("changed_display_name"));
		assertThat(account.getEmail(), is("changed_test@123.com"));
		assertThat(account.getLocale(), is("zh_CN"));
	}
	
	private Account getAccount(String json) {
		return ObjectMappers.readJSON(mapper, json, Account.class);
	}

}
