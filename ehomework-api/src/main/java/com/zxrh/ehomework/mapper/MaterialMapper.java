package com.zxrh.ehomework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import com.zxrh.ehomework.common.constant.MapperName;
import com.zxrh.ehomework.pojo.Material;

public interface MaterialMapper{

	@Select({
		"<script>",
			"select * from `material`",
			"<where>",
				"<if test='carrier != null'>",
					"and carrier = #{carrier}",
				"</if>",
				"<if test='subject != null'>",
					"and subject in",
					"<foreach collection='subject.relatedCodes' item='relatedCode' open='(' separator=',' close=')'>",
						"#{relatedCode}",
					"</foreach>",
				"</if>",
			"</where>",
		"</script>"
	})
	@Results(id=MapperName.MATERIAL,value={
		@Result(column="material_id",property="materialId",id=true),
		@Result(column="material_id",property="root",one=
			@One(select="com.zxrh.ehomework.mapper.UnitMapper.getRoot",fetchType=FetchType.EAGER))	
	})
	List<Material> findEligible(Material material);
	
	@Select({
		"<script>",
			"select * from `material`",
			"<if test='list != null'>",
				"where material_id in",
				"<foreach collection='list' item='materialId' index='index' open='(' separator=',' close=')'>",
					"#{materialId}",
				"</foreach>",
			"</if>",
		"</script>"
	})
	List<Material> getAll(List<Integer> materialIds);

	@Options(keyColumn="material_id",keyProperty="materialId",useGeneratedKeys=true)
	@Insert("insert into `material` (name,carrier,subject,created) values (#{name},#{carrier},#{subject},#{created})")
	void insert(Material material);
	
	@Select("select * from `material` where material_id = #{materialId}")
	Material get(Integer materialId);
	
	@Select("select * from `material` where material_id = #{materialId}")
	@Results(id=MapperName.DETAILED_MATERIAL,value={
		@Result(column="material_id",property="materialId",id=true),
		@Result(column="material_id",property="root",one=
			@One(select="com.zxrh.ehomework.mapper.UnitMapper.getRoot",fetchType=FetchType.EAGER))
	})
	Material getDetail(Integer materialId);

	
	
}
