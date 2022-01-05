package com.zxrh.ehomework.service;

import java.util.List;

import com.zxrh.ehomework.pojo.Answer;

public interface AnswerService{

	List<Answer> reviewByItem(Integer taskId);

	List<Answer> reviewItem(Integer taskId, Integer itemId);

	List<Answer> reviewHomework(Integer taskId, Integer homeworkId);

	Answer get(Integer answerId);

	Answer find(Integer homeworkId, Integer itemId);

	Answer getDetail(Integer answerId);

	List<Answer> findByTask(Integer taskId, Integer itemId);

}