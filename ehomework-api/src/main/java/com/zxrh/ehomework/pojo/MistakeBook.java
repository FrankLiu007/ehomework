package com.zxrh.ehomework.pojo;

import java.util.List;

public class MistakeBook{

	private Integer mistakeBookId;
	
	private Course course;
	
	private Student student;
	
	private List<Item> items;
	
	private int remind;

	public Integer getMistakeBookId(){
		return mistakeBookId;
	}

	public void setMistakeBookId(Integer mistakeBookId){
		this.mistakeBookId = mistakeBookId;
	}

	public Course getCourse(){
		return course;
	}

	public void setCourse(Course course){
		this.course = course;
	}

	public Student getStudent(){
		return student;
	}

	public void setStudent(Student student){
		this.student = student;
	}

	public List<Item> getItems(){
		return items;
	}

	public void setItems(List<Item> items){
		this.items = items;
	}

	public int getRemind(){
		return remind;
	}

	public void setRemind(int remind){
		this.remind = remind;
	}
	
}