package com.zxrh.ehomework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.zxrh.ehomework.pojo.Admin;

public interface AdminMapper{

	@Select("select * from `admin` where account = #{account}")
	Admin findByAccount(String account);

	@Select("select * from `admin` where id = #{adminId}")
	Admin get(Integer adminId);

	@Select("select * from `admin`")
	List<Admin> getAll();

	@Insert("insert into `admin` (account,password) values (#{account},#{password})")
	void create(Admin admin);

	@Delete("delete from `admin` where id = #{id}")
	void delete(Integer id);

}