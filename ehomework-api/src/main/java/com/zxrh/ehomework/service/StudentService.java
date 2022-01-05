package com.zxrh.ehomework.service;

import java.util.List;

import com.zxrh.ehomework.pojo.Homework;
import com.zxrh.ehomework.pojo.Student;
import com.zxrh.ehomework.pojo.Subject;
import com.zxrh.ehomework.pojo.Task.Form;

public interface StudentService{

	void create(Student student);

	List<Student> getAll(List<Integer> studentIds);
	
	Student get(Integer studentId);

	Integer countHomework(Homework homework);

	List<Homework> findHomeworksByCourse(Integer courseId, Integer studentId);

	Student findByAccount(String account);

	List<Student> findByCourse(Integer courseId);

	List<Homework> findAll(Homework homework);

	List<Homework> find(Student student,Form form,Boolean submitted,Subject subject);

	boolean findHomeworkRemind(Student student);

	boolean findTestRemind(Student student);

	boolean findMistakeRemind(Student student);

}