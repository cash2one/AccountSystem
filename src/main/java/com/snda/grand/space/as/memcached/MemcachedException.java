package com.snda.grand.space.as.memcached;

/**
 * 
 * @author wangzijian
 * 
 */
public class MemcachedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MemcachedException(String message, Throwable cause) {
		super(message, cause);
	}

	public MemcachedException(String message) {
		super(message);
	}

	public MemcachedException(Throwable cause) {
		super(cause);
	}

}
