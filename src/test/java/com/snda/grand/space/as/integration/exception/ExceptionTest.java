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
	public void testCreateAccountInvalidSndaIdException() throws Exception {
		
	}
	
	@Test
	public void testCreateAccountInvalidDisplayNameException() throws Exception {
		
	}
	
	@Test
	public void testCreateAccountInvalidUsernameNormExcpetion() throws Exception {
		
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
		
	}
	
	@Test
	public void testChangeAccountInvalidAvailableParamException() throws Exception {
		
	}
	
	private void assertError(String json, String code)
			throws Exception {
		ApplicationError error = mapper.readValue(json, ApplicationError.class);
		assertThat(error.getErrorCode(), is(code));
	}
	
}
