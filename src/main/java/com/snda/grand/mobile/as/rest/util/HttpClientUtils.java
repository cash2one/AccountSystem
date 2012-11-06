package com.snda.grand.mobile.as.rest.util;


import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

public class HttpClientUtils {
	private static final int DEFAULT_MAX_CONNECTION = 1000;
	private static final int DEFAULT_MAX_PER_ROUTE = 20;
	private static ThreadSafeClientConnManager cm = null;
	static {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory
				.getSocketFactory()));
		cm = new ThreadSafeClientConnManager(schemeRegistry);
		cm.setMaxTotal(DEFAULT_MAX_CONNECTION);
		cm.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
	}

	public static HttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000); // ms
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000); // ms
		params.setBooleanParameter("http.protocol.expect-continue", false);
		DefaultHttpClient client = new DefaultHttpClient(cm, params);
		HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(1, true);
		client.setHttpRequestRetryHandler(retryHandler);
		return client;
	}
	
	public static void release() {
		if (cm != null) {
			cm.shutdown();
		}
	}
}