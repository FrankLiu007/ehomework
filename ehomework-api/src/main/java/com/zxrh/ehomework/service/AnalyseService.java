package com.zxrh.ehomework.service;

import java.util.List;

import com.zxrh.ehomework.pojo.Choice;

public interface AnalyseService{

	Float getAverageScore(Integer taskId,Integer itemId);

	List<Choice> getGroupedChoices(Integer taskId, Integer questionId);

}