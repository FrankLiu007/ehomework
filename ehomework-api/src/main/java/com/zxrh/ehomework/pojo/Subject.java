package com.zxrh.ehomework.pojo;

import java.util.Arrays;
import java.util.List;

public enum Subject{

	CHINESE("语文",1,Arrays.asList(1)),MATHEMATICS("数学",2,Arrays.asList(2,3,4)),MATHS_ARTS("文数",3,Arrays.asList(2,3)),MATHS_SCIENCE("理数",4,Arrays.asList(2,4)),ENGLISH("英语",5,Arrays.asList(5)),
	INTEGRATION_ARTS("文综",6,Arrays.asList(6,7,8,9)),POLITICS("政治",7,Arrays.asList(6,7)),HISTORY("历史",8,Arrays.asList(6,8)),GEOGRAPHY("地理",9,Arrays.asList(6,9)),
	INTEGRATION_SCIENCE("理综",10,Arrays.asList(10,11,12,13)),PHYSICS("物理",11,Arrays.asList(10,11)),CHEMISTRY("化学",12,Arrays.asList(10,12)),BIOLOGY("生物",13,Arrays.asList(10,13));
	
	private String name;
	private Integer code;
	private List<Integer> relatedCodes;

	Subject(String name,Integer code,List<Integer> relatedCodes){
		this.name = name;
		this.code = code;
		this.relatedCodes = relatedCodes;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public Integer getCode(){
		return code;
	}

	public void setCode(Integer code){
		this.code = code;
	}
	
	public List<Integer> getRelatedCodes(){
		return relatedCodes;
	}

	public void setRelatedCodes(List<Integer> relatedCodes){
		this.relatedCodes = relatedCodes;
	}

	public static Subject getByCode(Integer code){
		for(Subject subject:values()){
			if(subject.code.equals(code)){
				return subject;
			}
		}
		return null;
	}
	
}