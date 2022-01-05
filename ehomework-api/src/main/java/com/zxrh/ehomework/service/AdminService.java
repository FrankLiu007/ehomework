package com.zxrh.ehomework.service;

import java.util.List;

import com.zxrh.ehomework.pojo.Admin;

public interface AdminService{

	Admin get(Integer adminId);

	Admin findByAccount(String account);

	List<Admin> getAll();

	void create(Admin admin);

	void delete(Integer id);

}