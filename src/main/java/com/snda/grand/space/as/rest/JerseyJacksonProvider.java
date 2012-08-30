package com.snda.grand.space.as.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.stereotype.Component;

import com.google.common.io.ByteStreams;
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;

/**
 * 
 * @author wangzijian
 * 
 */
@Component
@Provider
@Consumes(MediaType.WILDCARD)
@Produces(MediaType.APPLICATION_JSON)
public class JerseyJacksonProvider extends AbstractMessageReaderWriterProvider<Object> {

	private final ObjectWriter objectWriter; 
	private final ObjectMapper objectMapper;
	
	public JerseyJacksonProvider() {
		ObjectMapper objectMapper = new ObjectMapper();
		this.objectMapper = objectMapper;
		this.objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
	}
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}
	
	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException {
		return readFrom(type, entityStream);
	}
	

	@Override
	public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException {
		objectWriter.writeValue(entityStream, t);
	}
	
	public Object readFrom(Class<Object> type, InputStream entityStream) throws IOException {
		byte[] bytes = ByteStreams.toByteArray(entityStream);
		if (bytes.length == 0) {
			return null;
		}
		return objectMapper.readValue(bytes, type);
	}

}
