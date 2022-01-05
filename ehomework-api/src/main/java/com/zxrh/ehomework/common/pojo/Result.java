package com.zxrh.ehomework.common.pojo;

import java.util.HashMap;

import com.zxrh.ehomework.common.constant.MapKey;

public class Result<D> extends HashMap<String, Object>{
	
	private static final long serialVersionUID = 1L;

	@Override
	public Result<D> put(String key, Object value){
		if(!MapKey.CODE.equals(key) && !MapKey.MESSAGE.equals(key) && !MapKey.DATA.equals(key)){
			super.put(key, value);
		}
		return this;
	}
	
	public Result<D> code(Integer code){
		super.put(MapKey.CODE,code);
		return this;
	}
	
	public Integer code(){
		return (Integer)get(MapKey.CODE);
	}
	
	public Result<D> message(String message){
		super.put(MapKey.MESSAGE,message);
		return this;
	}
	
	public String message(){
		return (String)get(MapKey.MESSAGE);
	}
	
	public Result<D> data(D data){
		super.put(MapKey.DATA,data);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public D data(){
		return (D)get(MapKey.DATA);
	}
	
}
