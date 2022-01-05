package com.zxrh.ehomework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zxrh.ehomework.mapper.ItemMapper;
import com.zxrh.ehomework.mapper.OptionMapper;
import com.zxrh.ehomework.mapper.QuestionMapper;
import com.zxrh.ehomework.pojo.Item;
import com.zxrh.ehomework.pojo.Question;
import com.zxrh.ehomework.service.ItemService;

@Service
@Transactional
public class ItemServiceImpl implements ItemService{

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private QuestionMapper questionMapper;
	@Autowired
	private OptionMapper optionMapper;
	
	@Override
	public void create(Item item){
		itemMapper.insert(item);
		for(Question question:item.getQuestions()){
			question.setItem(item);
			questionMapper.insert(question);
			switch(question.getType()){
				case SINGLE:
				case MULTIPLE:
					optionMapper.insert(question);
					break;
				case GENERAL:
					break;
				default:
					break;
			}
		}
	}

	@Override
	public List<Item> findAll(){
		return itemMapper.findAll();
	}

	@Override
	public List<Item> getAll(List<Integer> itemIds){
		return itemMapper.getAll(itemIds);
	}

	@Override
	public List<Item> findByTask(Integer taskId){
		return itemMapper.findByTask(taskId);
	}

	@Override
	public List<Item> findByUnit(Integer unitId){
		return itemMapper.findByUnit(unitId);
	}

	@Override
	public List<Item> findUnreviewedByTask(Integer taskId){
		return itemMapper.findUnreviewedByTask(taskId);
	}

	@Override
	public Item getDetail(Integer itemId){
		return itemMapper.getDetail(itemId);
	}

	@Override
	public List<Item> findByMistakeBook(Integer mistakeBookId){
		return itemMapper.findByMistakeBook(mistakeBookId);
	}

}