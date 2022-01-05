package com.zxrh.ehomework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxrh.ehomework.service.RedisService;

@Service
public class RedisServiceImpl implements RedisService{

	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	@Autowired
	private ObjectMapper objectMapper;
	
	public void set(String key,String value){
		redisTemplate.opsForValue().set(key,value);
	}
	
	public String get(String key){
		return redisTemplate.opsForValue().get(key);
	}
	
	public void set(String key,Object object){
		set(key,serialize(object));
	}
	
	public <T> T get(String key,Class<T> clazz){
		return deserialize(get(key),clazz);
	}
	
	private String serialize(Object object){
		try{
			return objectMapper.writeValueAsString(object);
		}catch (JsonProcessingException e){
			return null;
		}
	}
	
	@Override
	public void delete(String key){
		redisTemplate.delete(key);
	}
	
	private <T> T deserialize(String string,Class<T> clazz){
		try{
			return objectMapper.readValue(string,clazz);
		}catch (Exception e){
			return null;
		}
	}

}