package com.zxrh.ehomework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.zxrh.ehomework.mapper.CourseMapper;
import com.zxrh.ehomework.mapper.MistakeBookMapper;
import com.zxrh.ehomework.pojo.Course;
import com.zxrh.ehomework.service.CourseService;

@Service
@Transactional
public class CourseServiceImpl implements CourseService{

	@Autowired
	private CourseMapper courseMapper;
	@Autowired
	private MistakeBookMapper mistakeBookMapper;
	
	@Override
	public Course get(Integer courseId){
		return courseMapper.get(courseId);
	}

	@Override
	public void create(Course course){
		courseMapper.insert(course);
		addStudents(course);
	}
	
	@Override
	public List<Course> findByTeacher(Integer teacherId){
		return courseMapper.findByTeacher(teacherId);
	}

	@Override
	public Course getDetail(Integer courseId){
		return courseMapper.getDetail(courseId);
	}

	@Override
	public List<Course> findByStudent(Integer studentId){
		return courseMapper.findByStudent(studentId);
	}

	@Override
	public void addStudents(Course course){
		if(!CollectionUtils.isEmpty(course.getStudents())){
			mistakeBookMapper.insert(course);
		}
	}

	@Override
	public List<Course> getAll(){
		return courseMapper.getAll();
	}

}