package com.zxrh.ehomework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zxrh.ehomework.mapper.MistakeBookMapper;
import com.zxrh.ehomework.pojo.MistakeBook;
import com.zxrh.ehomework.service.MistakeBookService;

@Service
@Transactional
public class MistakeBookServiceImpl implements MistakeBookService{

	@Autowired
	private MistakeBookMapper mistakeBookMapper;
	
	@Override
	public MistakeBook find(Integer studentId, Integer courseId){
		return mistakeBookMapper.find(studentId,courseId);
	}

	@Override
	public void collect(Integer mistakeBookId, Integer itemId){
		if(!isCollected(mistakeBookId,itemId)){
			mistakeBookMapper.collect(mistakeBookId,itemId);
		}
	}

	@Override
	public List<MistakeBook> findByStudent(Integer studentId){
		return mistakeBookMapper.findByStudent(studentId);
	}

	@Override
	public MistakeBook getDetail(Integer mistakeBookId){
		return mistakeBookMapper.getDetail(mistakeBookId);
	}

	@Override
	public void abandon(Integer mistakeBookId, Integer itemId){
		if(isCollected(mistakeBookId,itemId)){
			mistakeBookMapper.abandon(mistakeBookId,itemId);
		}
	}

	@Override
	public Boolean isCollected(Integer mistakeBookId,Integer itemId){
		return mistakeBookMapper.isCollected(mistakeBookId,itemId);
	}

	@Override
	public void clearRemind(Integer mistakeBookId, Integer itemId){
		mistakeBookMapper.clearRemind(mistakeBookId,itemId);
	}


}
