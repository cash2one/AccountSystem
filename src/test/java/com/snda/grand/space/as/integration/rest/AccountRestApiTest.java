package com.snda.grand.space.as.integration.rest;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.HttpHeaders;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public class AccountRestApiTest {

	private Client client;
	private final String DEFAULT_URI = "https://account.grandmobile.cn/";
	private final String AUTH = "NDJmNjhhZTFiMmQyYzAyY2MyZDY4MWNkMjBhMTc1ZGY6YTIxZTlkODYyN2ExMzZmZmYwZTk4ODZjMzJmNWE1NDU=";
	
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
	
	@After
	public void destroyClient() {
		client.destroy();
	}
	
	@Test
	public void getAccountStatus() {
		WebResource r = client.resource(DEFAULT_URI + "api/account/status/" + "1822847086");
		ClientResponse response = r.header(HttpHeaders.AUTHORIZATION, "Basic " + AUTH)
								   .get(ClientResponse.class);
		System.out.println(response.getStatus());
		System.out.println(response.getEntity(String.class));
	}

}
