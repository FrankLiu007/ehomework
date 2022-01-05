package com.zxrh.ehomework.service;

import java.util.List;

import com.zxrh.ehomework.pojo.Item;

public interface ItemService{

	void create(Item item);
	
	List<Item> findAll();
	
	List<Item> getAll(List<Integer> itemIds);

	List<Item> findByTask(Integer taskId);

	List<Item> findByUnit(Integer unitId);

	List<Item> findUnreviewedByTask(Integer taskId);

	Item getDetail(Integer itemId);

	List<Item> findByMistakeBook(Integer mistakeBookId);

}