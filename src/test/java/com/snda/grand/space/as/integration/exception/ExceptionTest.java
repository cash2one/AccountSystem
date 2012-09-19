package com.snda.grand.space.as.integration.exception;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.snda.grand.space.as.rest.model.ApplicationError;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ExceptionTest {
	
	private Client client;
	private final ObjectMapper mapper = new ObjectMapper();
	private final String DEFAULT_URI = "http://account.grandmobile.cn/";
	private final String APP_KEY = "ad2ce21541b07fcca852fff97e7e78f8";
	private final String APP_SECRET = "bd23ceb0e911729ff85fab246dd3e748";
	private final String AUTH = "YzNjYTQ1NDE4NTQyZTdlY2JjMzM3ZTI2NDY5ZWU4YTQ6ZWRhMzJkYzI3OWUxN2EyMDkxYWNjNTlhMTEyOWQ=";
	
	@Before
	public void createClient() {
		client = Client.create();
	}
	
	@After
	public void destroyClient() {
		client.destroy();
	}
	
	@Test
	public void testCreateAccountInvalidDisplayNameException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/account/create/1234567890");
		ClientResponse response = r.queryParam("username_norm", "TestUser")
								   .queryParam("email", "123@abc.com")
								   .header("Authorization", "Basic " + AUTH)
								   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(400));
		assertError(response.getEntity(String.class), "InvalidDisplayName");
	}
	
	@Test
	public void testCreateAccountInvalidUsernameNormExcpetion() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/account/create/1234567890");
		ClientResponse response = r.queryParam("display_name", "Test User")
								   .queryParam("email", "123@abc.com")
								   .header("Authorization", "Basic " + AUTH)
								   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(400));
		assertError(response.getEntity(String.class), "InvalidUsernameNorm");
	}
	
	@Test
	public void testCreateAccountInvalidEmailException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/account/create/1234567890");
		ClientResponse response = r.queryParam("username_norm", "TestUser")
								   .queryParam("display_name", "Test User")
								   .header("Authorization", "Basic " + AUTH)
								   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(400));
		assertError(response.getEntity(String.class), "InvalidEmail");
	}
	
	@Test
	public void testCreateAccountInvalidLocaleException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/account/create/1234567890");
		ClientResponse response = r.queryParam("username_norm", "TestUser")
								   .queryParam("display_name", "Test User")
								   .queryParam("email", "123@abc.com")
								   .queryParam("locale", "123")
								   .header("Authorization", "Basic " + AUTH)
								   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(400));
		assertError(response.getEntity(String.class), "InvalidLocale");
	}
	
	@Test
	public void testCreateAccountAlreadyExistException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/account/create/1533698109");
		ClientResponse response = r.queryParam("username_norm", "TestUser")
								   .queryParam("display_name", "Test User")
								   .queryParam("email", "123@abc.com")
								   .header("Authorization", "Basic " + AUTH)
								   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(409));
		assertError(response.getEntity(String.class), "AccountAlreadyExist");
	}
	
	@Test
	public void testChangeAccountInvalidAvailableParamException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/account/available/1533698109");
		ClientResponse response = r.queryParam("available", "123")
								   .header("Authorization", "Basic " + AUTH)
								   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(400));
		assertError(response.getEntity(String.class), "InvalidAvailableParam");
	}
	
	@Test
	public void testCreateApplicaitonInvalidUidException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/application/create/app123");
		ClientResponse response = r.header("Authorization", "Basic " + AUTH)
								   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(400));
		assertError(response.getEntity(String.class), "InvalidUid");
	}
	
	@Test
	public void testCreateApplicationInvalidAppDescriptionException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/application/create/app123");
		ClientResponse response = r.queryParam("uid", "uid123")
								   .header("Authorization", "Basic " + AUTH)
				   				   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(400));
		assertError(response.getEntity(String.class), "InvalidAppDescription");
	}
	
	@Test
	public void testCreateApplicationInvalidAppStatusException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/application/create/app123");
		ClientResponse response = r.queryParam("uid", "uid123")
								   .queryParam("app_description", "appdesc123")
								   .queryParam("app_status", "appstatus123")
								   .header("Authorization", "Basic " + AUTH)
				   				   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(400));
		assertError(response.getEntity(String.class), "InvalidAppStatus");
	}
	
	@Test
	public void testCreateApplicationInvalidScopeException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/application/create/app123");
		ClientResponse response = r.queryParam("uid", "uid123")
								   .queryParam("app_description", "appdesc123")
								   .queryParam("app_status", "development")
								   .queryParam("scope", "scope123")
								   .header("Authorization", "Basic " + AUTH)
				   				   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(400));
		assertError(response.getEntity(String.class), "InvalidScope");
	}
	
	@Test
	public void testCreateApplicationInvalidWebSiteException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/application/create/app123");
		ClientResponse response = r.queryParam("uid", "uid123")
								   .queryParam("app_description", "appdesc123")
								   .queryParam("app_status", "development")
								   .queryParam("scope", "full")
								   .queryParam("website", "123123")
								   .header("Authorization", "Basic " + AUTH)
				   				   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(400));
		assertError(response.getEntity(String.class), "InvalidWebSite");
	}
	
	@Test
	public void testCreateApplicationNoSuchAccountException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/application/create/app123");
		ClientResponse response = r.queryParam("uid", "uid123")
								   .queryParam("app_description", "appdesc123")
								   .queryParam("app_status", "development")
								   .queryParam("scope", "full")
								   .queryParam("website", "123.com")
								   .header("Authorization", "Basic " + AUTH)
				   				   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(404));
		assertError(response.getEntity(String.class), "NoSuchAccount");
	}
	
	@Test
	public void testCreateApplicationApplicationAlreadyExistException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/application/create/www");
		ClientResponse response = r.queryParam("uid", "0")
								   .queryParam("app_description", "appdesc123")
								   .queryParam("app_status", "development")
								   .queryParam("scope", "full")
								   .queryParam("website", "123.com")
								   .header("Authorization", "Basic " + AUTH)
				   				   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(409));
		assertError(response.getEntity(String.class), "ApplicationAlreadyExist");
	}
	
	@Test
	public void testListAuthorizedInvalidRequestParamsException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/application/authorized/abc123");
		ClientResponse response = r.header("Authorization", "Basic " + AUTH)
								   .get(ClientResponse.class);
		assertThat(response.getStatus(), is(400));
		assertError(response.getEntity(String.class), "InvalidRequestParams");
	}
	
	@Test
	public void testListAuthorizedNoSuchApplicationException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/application/authorized/abc123");
		ClientResponse response = r.queryParam("owner", "0")
								   .header("Authorization", "Basic " + AUTH)
								   .get(ClientResponse.class);
		assertThat(response.getStatus(), is(404));
		assertError(response.getEntity(String.class), "NoSuchApplication");
	}
	
	@Test
	public void testChangeStatusInvalidAppStatusException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/application/status/abc123");
		ClientResponse response = r.queryParam("owner", "shit")
								   .queryParam("app_status", "123123123")
								   .header("Authorization", "Basic " + AUTH)
								   .post(ClientResponse.class);
		assertThat(response.getStatus(), is(400));
		assertError(response.getEntity(String.class), "InvalidAppStatus");
	}
	
	@Test
	public void testCancelAuthorizationInvalidRequestParamsException() throws Exception {
		WebResource r = client.resource(DEFAULT_URI + "api/application/token");
		ClientResponse response = r.queryParam("uid", "123")
								   .header("Authorization", "Basic " + AUTH)
								   .delete(ClientResponse.class);
		assertThat(response.getStatus(), is(400));
		assertError(response.getEntity(String.class), "InvalidRequestParams");
	}
	
	private void assertError(String json, String code)
			throws Exception {
		System.out.println(json);
		ApplicationError error = mapper.readValue(json, ApplicationError.class);
		assertThat(error.getErrorCode(), is(code));
	}
	
}
