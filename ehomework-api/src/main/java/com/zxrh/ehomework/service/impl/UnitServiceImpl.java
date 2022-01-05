package com.zxrh.ehomework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zxrh.ehomework.mapper.UnitMapper;
import com.zxrh.ehomework.pojo.Unit;
import com.zxrh.ehomework.service.UnitService;

@Service
@Transactional
public class UnitServiceImpl implements UnitService{
	
	@Autowired
	private UnitMapper unitMapper;

	@Override
	public Unit get(Integer unitId){
		return unitMapper.get(unitId);
	}

	@Override
	public Unit getDetail(Integer unitId){
		return unitMapper.getDetail(unitId);
	}

	@Override
	public List<Unit> findByParent(Integer unitId){
		return unitMapper.findByParent(unitId);
	}

}
