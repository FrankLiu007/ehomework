package com.zxrh.ehomework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zxrh.ehomework.mapper.HomeworkMapper;
import com.zxrh.ehomework.mapper.TaskMapper;
import com.zxrh.ehomework.pojo.Homework;
import com.zxrh.ehomework.pojo.Homework.Status;
import com.zxrh.ehomework.pojo.Item;
import com.zxrh.ehomework.pojo.Student;
import com.zxrh.ehomework.pojo.Task;
import com.zxrh.ehomework.service.TaskService;

@Service
@Transactional
public class TaskServiceImpl implements TaskService{

	@Autowired
	private TaskMapper taskMapper;
	@Autowired
	private HomeworkMapper homeworkMapper;
	
	@Override
	public void create(Task task){
		taskMapper.insert(task);
		taskMapper.addItems(task);
		for(Student student:task.getCourse().getStudents()){
			Homework homework = new Homework();
			homework.setTask(task);
			homework.setStudent(student);
			homework.setStatus(Status.UNSUBMITTED);
			homework.setRemind(true);
			homeworkMapper.insert(homework);
		}
	}

	@Override
	public void addItems(Integer taskId, List<Integer> itemIds){
	}

	@Override
	public List<Task> findByCourse(Integer courseId){
		return taskMapper.findByCourse(courseId);
	}

	@Override
	public List<Item> findUnreviewedItems(Integer taskId){
		return null;
	}

	@Override
	public List<Task> findCompliant(Task task){
		return taskMapper.findCompliant(task);
	}

	@Override
	public Task getDetail(Integer taskId){
		return taskMapper.getDetail(taskId);
	}

}
