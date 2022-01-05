package com.zxrh.ehomework.service;

import java.util.List;

import com.zxrh.ehomework.pojo.Homework;
import com.zxrh.ehomework.pojo.Subject;
import com.zxrh.ehomework.pojo.Task.Form;

public interface HomeworkService{

	void create(Homework homework);

	Homework getDetail(Integer homeworkId);
	
	int submit(Homework homework);

	List<Homework> findSubmittedByTask(Integer taskId);

	List<Homework> findCompliant(Homework homework);

	List<Homework> findByTask(Integer taskId);

	void update(Homework homework);

	int countRemind(Integer studentId, Form form, Boolean submitted, Subject subject);

	List<Homework> reviewByHomework(Integer taskId);

	void correctIfCorrected(Homework homework);
	
}