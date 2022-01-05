package com.zxrh.ehomework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zxrh.ehomework.mapper.MaterialMapper;
import com.zxrh.ehomework.mapper.UnitMapper;
import com.zxrh.ehomework.pojo.Item;
import com.zxrh.ehomework.pojo.Material;
import com.zxrh.ehomework.pojo.Unit;
import com.zxrh.ehomework.service.ItemService;
import com.zxrh.ehomework.service.MaterialService;

@Service
@Transactional
public class MaterialServiceImpl implements MaterialService{

	@Autowired
	private MaterialMapper materialMapper;
	@Autowired
	private UnitMapper unitMapper;
	@Autowired
	private ItemService itemService;
	
	@Override
	public List<Material> findEligible(Material material){
		return materialMapper.findEligible(material);
	}
	
	@Override
	public List<Material> getAll(List<Integer> materialIds){
		return materialMapper.getAll(materialIds);
	}

	@Override
	public void create(Material material){
		materialMapper.insert(material);
		insertUnitsAndItems(material);
	}

	@Override
	public void createPaper(Material material){
		materialMapper.insert(material);
		insertUnitsAndItems(material);
	}

	private void insertUnitsAndItems(Material material){
		Unit root = material.getRoot();
		root.setMaterial(material);
		recurse(root);
	}
	
	private void recurse(Unit parent){
		unitMapper.insert(parent);
		List<Unit> units = parent.getUnits();
		if(units == null || units.isEmpty()){
			List<Item> items = parent.getItems();
			if(items != null && !items.isEmpty()){
				for(Item item:items){
					item.setUnit(parent);
					itemService.create(item);
				}
			}
		}else{
			for(Unit unit:units){
				unit.setMaterial(parent.getMaterial());
				unit.setParent(parent);
				recurse(unit);
			}
		}
	}

	

	
	
}
