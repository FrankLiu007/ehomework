package com.zxrh.ehomework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import com.zxrh.ehomework.common.constant.MapperName;
import com.zxrh.ehomework.pojo.Unit;

public interface UnitMapper{

	@Select("select * from `unit` where unit_id = #{unitId}")
	@Results(id=MapperName.UNIT,value={
		@Result(column="unit_id",property="unitId",id=true),
		@Result(column="unit_id",property="isParent",one=
			@One(select="com.zxrh.ehomework.mapper.UnitMapper.isParent",fetchType=FetchType.EAGER))
	})
	Unit get(Integer unitId);
	
	@Select("select * from `unit` where unit_id = #{unitId}")
	@Results(id=MapperName.DETAILED_UNIT,value={
		@Result(column="unit_id",property="unitId",id=true),
		@Result(column="material_id",property="material",one=
			@One(select="com.zxrh.ehomework.mapper.MaterialMapper.get",fetchType=FetchType.EAGER)),
		@Result(column="parent_id",property="parent",one=
			@One(select="com.zxrh.ehomework.mapper.UnitMapper.get",fetchType=FetchType.EAGER)),
		@Result(column="unit_id",property="isParent",one=
			@One(select="com.zxrh.ehomework.mapper.UnitMapper.isParent",fetchType=FetchType.EAGER)),
		@Result(column="unit_id",property="items",many=
			@Many(select="com.zxrh.ehomework.mapper.ItemMapper.findByUnit",fetchType=FetchType.EAGER))
	})
	Unit getDetail(Integer unitId);

	@Select("select (select count(*) from `unit` where parent_id = #{unitId}) > 0")
	Boolean isParent(Integer unitId);
	
	@Select("select * from `unit` where material_id = #{materialId} and parent_id is null")
	@ResultMap(MapperName.UNIT)
	Unit getRoot(Integer materialId);

	@Options(keyColumn="unit_id",keyProperty="unitId",useGeneratedKeys=true)
	@Insert("insert into `unit` (material_id,name,parent_id) values (#{material.materialId},#{name},#{parent.unitId})")
	void insert(Unit unit);

	@Select("select * from `unit` where parent_id = #{unitId}")
	@ResultMap(MapperName.UNIT)
	List<Unit> findByParent(Integer unitId);
	
}
