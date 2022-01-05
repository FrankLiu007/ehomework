package com.zxrh.ehomework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zxrh.ehomework.mapper.TeacherMapper;
import com.zxrh.ehomework.pojo.Course;
import com.zxrh.ehomework.pojo.Teacher;
import com.zxrh.ehomework.service.TeacherService;

@Service
public class TeacherServiceImpl implements TeacherService{

	@Autowired
	private TeacherMapper teacherMapper;
	
	@Override
	public void create(Teacher teacher){
		teacherMapper.insert(teacher);
	}

	@Override
	public List<Course> findCoursesByTeacher(Integer teacherId){
		return null;
	}

	@Override
	public Teacher get(Integer teacherId){
		return teacherMapper.get(teacherId);
	}

	@Override
	public Teacher findByAccount(String account){
		return teacherMapper.findByAccount(account);
	}

	@Override
	public List<Teacher> getAll(){
		return teacherMapper.getAll();
	}

	@Override
	public List<Teacher> search(String keyword){
		return teacherMapper.search(keyword);
	}

	@Override
	public void update(Teacher teacher){
		teacherMapper.update(teacher);
	}

}
