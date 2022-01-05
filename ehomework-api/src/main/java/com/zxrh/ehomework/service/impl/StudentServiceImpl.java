package com.zxrh.ehomework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zxrh.ehomework.mapper.StudentMapper;
import com.zxrh.ehomework.pojo.Homework;
import com.zxrh.ehomework.pojo.Student;
import com.zxrh.ehomework.pojo.Subject;
import com.zxrh.ehomework.pojo.Task.Form;
import com.zxrh.ehomework.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService{
	
	@Autowired
	private StudentMapper studentMapper;

	@Override
	public void create(Student student){
		studentMapper.insert(student);
	}

	@Override
	public List<Student> getAll(List<Integer> studentIds){
		return studentMapper.getAll(studentIds);
	}

	@Override
	public Student get(Integer studentId){
		return studentMapper.get(studentId);
	}

	@Override
	public Integer countHomework(Homework homework){
		return studentMapper.countHomework(homework);
	}

	@Override
	public List<Homework> findHomeworksByCourse(Integer courseId, Integer studentId){
		return studentMapper.findHomeworksByCourse(courseId,studentId);
	}

	@Override
	public Student findByAccount(String account){
		return studentMapper.findByAccount(account);
	}

	@Override
	public List<Student> findByCourse(Integer courseId){
		return studentMapper.findByCourse(courseId);
	}

	@Override
	public List<Homework> findAll(Homework homework){
		return studentMapper.findAll(homework);
	}

	@Override
	public List<Homework> find(Student student,Form form, Boolean submitted, Subject subject){
		return studentMapper.find(student,form,submitted,subject);
	}

	@Override
	public boolean findHomeworkRemind(Student student){
		return studentMapper.findHomeworkRemind(student);
	}

	@Override
	public boolean findTestRemind(Student student){
		return studentMapper.findTestRemind(student);
	}

	@Override
	public boolean findMistakeRemind(Student student){
		return studentMapper.findMistakeRemind(student);
	}

}