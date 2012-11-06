package com.snda.grand.space.as.integration.mongo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.spy.memcached.MemcachedClient;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.google.common.collect.Maps;
import com.snda.grand.mobile.as.mongo.internal.model.Accessor;

public class MemcachedTest {

	@Test
	public void test() {
		ApplicationContext ctx = new FileSystemXmlApplicationContext("src/test/resources/application.memcached.xml");
		MemcachedClient client = (MemcachedClient) ctx.getBean("memcachedClient");
		client.set("someKey", 3600, "fuckValue");
		
		Object object = client.get("someKey");
		System.out.println(object);
	}
	
	@Test
	public void testConvert() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json = "{\"id\":\"504da6cdc5067e8ec6bfb190\",\"username\":\"test\",\"accessKey\":\"c3ca45418542e7ecbc337e26469ee8a4\",\"secretKey\":\"eda32dc279e17a2091acc59a1129d\",\"description\":\"For test\",\"creationTime\":1347266252947}";
		Accessor accessor = mapper.readValue(json, Accessor.class);
		System.out.println(accessor);
	}
	
	@Test
	public void testLocale() {
		Locale list[] = SimpleDateFormat.getAvailableLocales();
		Map<String, Locale> map = Maps.newHashMap();
		for (int i = 0; i < list.length; i++) {
			map.put(list[i].toString(), list[i]);
		}
	}

}
