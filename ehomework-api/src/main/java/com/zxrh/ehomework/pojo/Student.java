package com.zxrh.ehomework.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Student{

	private Integer studentId;
	
	private String account;
	
	@JsonIgnore
	private String password;
	
	private String name;

	public Integer getStudentId(){
		return studentId;
	}

	public void setStudentId(Integer studentId){
		this.studentId = studentId;
	}

	public String getAccount(){
		return account;
	}

	public void setAccount(String account){
		this.account = account;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

}
