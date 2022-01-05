package com.zxrh.ehomework.service;

import java.util.List;

import com.zxrh.ehomework.pojo.Unit;

public interface UnitService{

	Unit get(Integer unitId);

	Unit getDetail(Integer unitId);

	List<Unit> findByParent(Integer unitId);

}