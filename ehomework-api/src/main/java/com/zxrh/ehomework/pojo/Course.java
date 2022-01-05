package com.zxrh.ehomework.pojo;

import java.util.List;

public class Course{

	private Integer courseId;
	
	private String name;
	
	private Teacher teacher;
	
	private List<Student> students;
	
	private List<Task> tasks;
	
	private Integer studentNumber;
	
	private Integer taskNumber;
	
	private Boolean pending;
	
	public Integer getCourseId(){
		return courseId;
	}

	public void setCourseId(Integer courseId){
		this.courseId = courseId;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public Teacher getTeacher(){
		return teacher;
	}

	public void setTeacher(Teacher teacher){
		this.teacher = teacher;
	}

	public List<Student> getStudents(){
		return students;
	}

	public void setStudents(List<Student> students){
		this.students = students;
	}

	public List<Task> getTasks(){
		return tasks;
	}

	public void setTasks(List<Task> tasks){
		this.tasks = tasks;
	}

	public Integer getStudentNumber(){
		return studentNumber;
	}

	public void setStudentNumber(Integer studentNumber){
		this.studentNumber = studentNumber;
	}

	public Integer getTaskNumber(){
		return taskNumber;
	}

	public void setTaskNumber(Integer taskNumber){
		this.taskNumber = taskNumber;
	}

	public Boolean getPending(){
		return pending;
	}

	public void setPending(Boolean pending){
		this.pending = pending;
	}
	
}