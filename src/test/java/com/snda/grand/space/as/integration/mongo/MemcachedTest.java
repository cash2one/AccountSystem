package com.snda.grand.space.as.integration.mongo;

import net.spy.memcached.MemcachedClient;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class MemcachedTest {

	@Test
	public void test() {
		ApplicationContext ctx = new FileSystemXmlApplicationContext("src/test/resources/application.memcached.xml");
		MemcachedClient client = (MemcachedClient) ctx.getBean("memcachedClient");
		client.set("someKey", 3600, "fuckValue");
		
		Object object = client.get("someKey");
		System.out.println(object);
	}

}
