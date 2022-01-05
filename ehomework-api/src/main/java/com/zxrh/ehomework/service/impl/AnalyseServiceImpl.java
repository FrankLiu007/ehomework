package com.zxrh.ehomework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zxrh.ehomework.mapper.AnalyseMapper;
import com.zxrh.ehomework.pojo.Choice;
import com.zxrh.ehomework.service.AnalyseService;

@Service
@Transactional
public class AnalyseServiceImpl implements AnalyseService{
	
	@Autowired
	private AnalyseMapper analyseMapper;
	
	@Override
	public Float getAverageScore(Integer taskId, Integer itemId){
		return analyseMapper.getAverageScore(taskId,itemId);
	}

	@Override
	public List<Choice> getGroupedChoices(Integer taskId, Integer questionId){
		return analyseMapper.getGroupedChoices(taskId,questionId);
	}

}