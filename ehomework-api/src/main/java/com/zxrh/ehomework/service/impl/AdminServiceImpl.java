package com.zxrh.ehomework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zxrh.ehomework.mapper.AdminMapper;
import com.zxrh.ehomework.pojo.Admin;
import com.zxrh.ehomework.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService{

	@Autowired
	private AdminMapper adminMapper;
	
	@Override
	public Admin get(Integer adminId){
		return adminMapper.get(adminId);
	}

	@Override
	public Admin findByAccount(String account){
		return adminMapper.findByAccount(account);
	}

	@Override
	public List<Admin> getAll(){
		return adminMapper.getAll();
	}

	@Override
	public void create(Admin admin){
		adminMapper.create(admin);
	}

	@Override
	public void delete(Integer id){
		adminMapper.delete(id);
	}

}
