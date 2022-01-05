package com.zxrh.ehomework.service;

public interface RedisService{
	
	void set(String key,String value);
	
	String get(String key);
	
	void set(String key,Object object);
	
	<T> T get(String key,Class<T> clazz);

	void delete(String key);

}
