package com.zxrh.ehomework.service;

import java.util.List;

import com.zxrh.ehomework.pojo.Course;

public interface CourseService{

	Course get(Integer courseId);
	
	void create(Course course);
	
	void addStudents(Course course);

	List<Course> findByTeacher(Integer teacherId);

	Course getDetail(Integer courseId);

	List<Course> findByStudent(Integer studentId);

	List<Course> getAll();

}