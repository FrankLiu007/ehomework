package com.zxrh.ehomework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zxrh.ehomework.mapper.AnswerMapper;
import com.zxrh.ehomework.pojo.Answer;
import com.zxrh.ehomework.service.AnswerService;

@Service
@Transactional
public class AnswerServiceImpl implements AnswerService{
	
	@Autowired
	private AnswerMapper answerMapper;
	
	@Override
	public List<Answer> reviewByItem(Integer taskId){
		return answerMapper.reviewByItem(taskId);
	}

	@Override
	public List<Answer> reviewItem(Integer taskId, Integer itemId){
		return answerMapper.reviewItem(taskId,itemId);
	}

	@Override
	public List<Answer> reviewHomework(Integer taskId, Integer homeworkId){
		return answerMapper.reviewHomework(taskId,homeworkId);
	}

	@Override
	public Answer get(Integer answerId){
		return answerMapper.get(answerId);
	}

	@Override
	public Answer find(Integer homeworkId, Integer itemId){
		return answerMapper.find(homeworkId,itemId);
	}

	@Override
	public Answer getDetail(Integer answerId){
		return answerMapper.getDetail(answerId);
	}

	@Override
	public List<Answer> findByTask(Integer taskId, Integer itemId){
		return answerMapper.findByTask(taskId,itemId);
	}

}