package com.zxrh.ehomework.service;

import java.util.List;

import com.zxrh.ehomework.pojo.Item;
import com.zxrh.ehomework.pojo.Task;

public interface TaskService{

	void create(Task task);
	
	void addItems(Integer taskId,List<Integer> itemIds);

	List<Task> findByCourse(Integer courseId);

	List<Item> findUnreviewedItems(Integer taskId);

	List<Task> findCompliant(Task task);

	Task getDetail(Integer taskId);
	
}
