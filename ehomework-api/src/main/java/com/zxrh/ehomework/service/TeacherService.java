package com.zxrh.ehomework.service;

import java.util.List;

import com.zxrh.ehomework.pojo.Course;
import com.zxrh.ehomework.pojo.Teacher;

public interface TeacherService{

	void create(Teacher teacher);
	
	List<Course> findCoursesByTeacher(Integer teacherId);
	
	Teacher get(Integer teacherId);

	Teacher findByAccount(String account);

	List<Teacher> getAll();

	List<Teacher> search(String keyword);

	void update(Teacher teacher);
	
}