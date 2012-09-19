package com.snda.grand.space.as.integration.exception;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.snda.grand.space.as.rest.model.Application;
import com.snda.grand.space.as.rest.util.ObjectMappers;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ApplicationTest {
	
	private Client client;
	private final ObjectMapper mapper = new ObjectMapper();
	private final String DEFAULT_URI = "http://account.grandmobile.cn/";
	private final String APP_KEY = "c3ca45418542e7ecbc337e26469ee8a4";
	private final String APP_SECRET = "eda32dc279e17a2091acc59a1129d";
	private final String AUTH = Base64.encodeBase64String((APP_KEY + ":" + APP_SECRET).getBytes());
	
	private final String TEST_APP_APPID = "test_app_appid";
	private final String TEST_APP_OWNER = "0";
	private final String TEST_APP_APP_DESCRIPTION = "test_app_app_description";
	private final String TEST_APP_APP_STATUS = "development";
	private final String TEST_APP_PUBLISHER_NAME = "test_app_publisher_name";
	private final String TEST_APP_SCOPE = "full";
	private final String TEST_APP_WEBSITE = "www.123.com";
	
	@Before
	public void createClient() {
		client = Client.create();
	}
	
	@After
	public void destroyClient() {
		client.destroy();
	}

	@Test
	public void test() {
		createTestApplication();
		modifyTestApplication();
		getTestApplicationStatus();
		changeTestApplicationStatus();
		deleteTestApplication();
	}

	private void createTestApplication() {
		WebResource r = client.resource(DEFAULT_URI + "api/application/create/" + TEST_APP_APPID);
		ClientResponse response = r.queryParam("uid", TEST_APP_OWNER)
								   .queryParam("app_description", TEST_APP_APP_DESCRIPTION)
								   .queryParam("app_status", TEST_APP_APP_STATUS)
								   .queryParam("publisher_name", TEST_APP_PUBLISHER_NAME)
								   .queryParam("scope", TEST_APP_SCOPE)
								   .queryParam("website", TEST_APP_WEBSITE)
								   .header(HttpHeaders.AUTHORIZATION, "Basic " + AUTH)
								   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(200));
		
		Application application = getApplication(response.getEntity(String.class));
		assertThat(application.getAppid(), is(TEST_APP_APPID));
		assertThat(application.getOwner(), is(TEST_APP_OWNER));
		assertThat(application.getAppDescription(), is(TEST_APP_APP_DESCRIPTION));
		assertThat(application.getAppStatus(), is(TEST_APP_APP_STATUS));
		assertThat(application.getPublisherName(), is(TEST_APP_PUBLISHER_NAME));
		assertThat(application.getScope(), is(TEST_APP_SCOPE));
		assertThat(application.getWebsite(), is(TEST_APP_WEBSITE));
	}
	
	private void modifyTestApplication() {
		WebResource r = client.resource(DEFAULT_URI + "api/application/modify/" + TEST_APP_APPID);
		ClientResponse response = r.queryParam("owner", TEST_APP_OWNER)
								   .queryParam("app_description", "changed_test_app_app_decription")
								   .queryParam("website", "www.changed.com")
								   .header(HttpHeaders.AUTHORIZATION, "Basic " + AUTH)
								   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(200));
		
		Application application = getApplication(response.getEntity(String.class));
		assertThat(application.getAppid(), is(TEST_APP_APPID));
		assertThat(application.getOwner(), is(TEST_APP_OWNER));
		assertThat(application.getAppDescription(), is("changed_test_app_app_decription"));
		assertThat(application.getAppStatus(), is(TEST_APP_APP_STATUS));
		assertThat(application.getPublisherName(), is(TEST_APP_PUBLISHER_NAME));
		assertThat(application.getScope(), is(TEST_APP_SCOPE));
		assertThat(application.getWebsite(), is("www.changed.com"));
	}
	
	private void getTestApplicationStatus() {
		WebResource r = client.resource(DEFAULT_URI + "api/application/status/" + TEST_APP_APPID);
		ClientResponse response = r.queryParam("owner", TEST_APP_OWNER)
								   .header(HttpHeaders.AUTHORIZATION, "Basic " + AUTH)
								   .get(ClientResponse.class);
		assertThat(response.getStatus(), is(200));
		
		Application application = getApplication(response.getEntity(String.class));
		assertThat(application.getAppid(), is(TEST_APP_APPID));
		assertThat(application.getOwner(), is(TEST_APP_OWNER));
		assertThat(application.getAppDescription(), is("changed_test_app_app_decription"));
		assertThat(application.getAppStatus(), is(TEST_APP_APP_STATUS));
		assertThat(application.getPublisherName(), is(TEST_APP_PUBLISHER_NAME));
		assertThat(application.getScope(), is(TEST_APP_SCOPE));
		assertThat(application.getWebsite(), is("www.changed.com"));
	}
	
	private void changeTestApplicationStatus() {
		WebResource r = client.resource(DEFAULT_URI + "api/application/status/" + TEST_APP_APPID);
		ClientResponse response = r.queryParam("owner", TEST_APP_OWNER)
								   .queryParam("app_status", "release")
								   .header(HttpHeaders.AUTHORIZATION, "Basic " + AUTH)
								   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(200));
		
		Application application = getApplication(response.getEntity(String.class));
		assertThat(application.getAppid(), is(TEST_APP_APPID));
		assertThat(application.getOwner(), is(TEST_APP_OWNER));
		assertThat(application.getAppDescription(), is("changed_test_app_app_decription"));
		assertThat(application.getAppStatus(), is("release"));
		assertThat(application.getPublisherName(), is(TEST_APP_PUBLISHER_NAME));
		assertThat(application.getScope(), is(TEST_APP_SCOPE));
		assertThat(application.getWebsite(), is("www.changed.com"));
	}
	
	private void deleteTestApplication() {
		WebResource r = client.resource(DEFAULT_URI + "api/application/delete/" + TEST_APP_APPID);
		ClientResponse response = r.queryParam("owner", TEST_APP_OWNER)
								   .header(HttpHeaders.AUTHORIZATION, "Basic " + AUTH)
								   .delete(ClientResponse.class);
		assertThat(response.getStatus(), is(204));
	}
	
	private Application getApplication(String json) {
		return ObjectMappers.readJSON(mapper, json, Application.class);
	}
	
}
