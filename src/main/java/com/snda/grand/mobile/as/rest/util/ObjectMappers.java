package com.snda.grand.mobile.as.rest.util;

import java.io.StringWriter;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


/**
 * 
 * 
 * @author wangzijian
 * 
 */
public class ObjectMappers {

	private ObjectMappers() {
	}

	public static String toJSON(ObjectMapper objectMapper, Object object) {
		StringWriter writer = new StringWriter();
		try {
			objectMapper.writeValue(writer, object);
		} catch (Exception e) {
			throw new JSONException("Convert java " + object + " to json error.", e);
		}
		return writer.toString();
	}

	public static <T> T readJSON(ObjectMapper objectMapper, String string, Class<T> valueType) {
		try {
			return (T) objectMapper.readValue(string, valueType);
		} catch (Exception e) {
			throw new JSONException("Convert json '" + string + "' to " + valueType + " error.", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T readJSON(ObjectMapper objectMapper, String string, TypeReference<T> typeReference) {
		try {
			return (T) objectMapper.readValue(string, typeReference);
		} catch (Exception e) {
			throw new JSONException("Convert json '" + string + "' to " + typeReference + " error.", e);
		}
	}
}
