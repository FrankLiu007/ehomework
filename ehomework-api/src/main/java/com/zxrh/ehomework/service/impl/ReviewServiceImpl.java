package com.zxrh.ehomework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zxrh.ehomework.mapper.AnswerMapper;
import com.zxrh.ehomework.mapper.HomeworkMapper;
import com.zxrh.ehomework.mapper.ReviewMapper;
import com.zxrh.ehomework.pojo.Homework;
import com.zxrh.ehomework.pojo.Review;
import com.zxrh.ehomework.service.HomeworkService;
import com.zxrh.ehomework.service.ReviewService;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService{

	@Autowired
	private ReviewMapper reviewMapper;
	@Autowired
	private AnswerMapper answerMapper;
	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private HomeworkMapper homeworkMapper;
	
	@Override
	public Review findByAnswer(Integer answerId){
		return reviewMapper.findByAnswer(answerId);
	}

	@Override
	public void insert(Review review){
		reviewMapper.insert(review);
		Integer answerId = review.getAnswerId();
		answerMapper.review(answerId);
		Homework homework = homeworkMapper.findByAnswer(answerId);
		homeworkService.correctIfCorrected(homework);
	}

	@Override
	public void update(Review review){
		reviewMapper.update(review);
		Integer answerId = review.getAnswerId();
		answerMapper.review(answerId);
		Homework homework = homeworkMapper.findByAnswer(answerId);
		homeworkService.correctIfCorrected(homework);
	}
	
}