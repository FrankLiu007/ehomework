package com.zxrh.ehomework.service;

import java.util.List;

import com.zxrh.ehomework.pojo.Material;

public interface MaterialService{
	
	List<Material> findEligible(Material material);

	List<Material> getAll(List<Integer> materialIds);
	
	void create(Material material);

	void createPaper(Material material);
	
}