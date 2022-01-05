package com.zxrh.ehomework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import com.zxrh.ehomework.common.constant.MapperName;
import com.zxrh.ehomework.pojo.Item;

public interface ItemMapper{

	@Select("select * from `item` where item_id = #{itemId}")
	Item get(Integer itemId);
	
	@Select("select * from `item` where item_id = #{itemId}")
	@Results(id=MapperName.ITEM,value={
		@Result(column="item_id",property="itemId",id=true),
		@Result(column="item_id",property="questions",many=
			@Many(select="com.zxrh.ehomework.mapper.QuestionMapper.findByItem",fetchType=FetchType.EAGER))
	})
	Item getDetail(Integer itemId);
	
	@Options(keyColumn="item_id",keyProperty="itemId",useGeneratedKeys=true)
	@Insert("insert into `item` (category,title,unit_id,reference) values (#{category},#{title},#{unit.unitId},#{reference})")
	void insert(Item item);
	
	@Select("select * from `item`")
	@ResultMap(MapperName.ITEM)
	List<Item> findAll();
	
	@Select({"<script>",
		"select * from `item` where item_id in ",
		"<foreach collection='list' item='itemId' index='index' open='(' separator=',' close=')'>",
			"#{itemId}",
		"</foreach>",
	"</script>"})
	List<Item> getAll(List<Integer> itemIds);

	@Select({"select * from `item` inner join `task_item` ",
		"on `item`.item_id = `task_item`.item_id ",
		"where `task_item`.task_id = #{taskId}"})
	@ResultMap(MapperName.ITEM)
	List<Item> findByTask(Integer taskId);
	
	@Select({
		"select * from `item`",
		"inner join `mistake_book_item` on `item`.item_id = `mistake_book_item`.item_id",
		"where `mistake_book_item`.mistake_book_id = #{mistakeBookId}",
		"order by `mistake_book_item`.remind desc"
	})
	@Results({
		@Result(column="item_id",property="itemId",id=true),
		@Result(column="item_id",property="questions",many=
			@Many(select="com.zxrh.ehomework.mapper.QuestionMapper.findByItem",fetchType=FetchType.EAGER))
	})
	List<Item> findByMistakeBook(Integer mistakeBookId);

	@Select("select * from `item` where unit_id = #{unitId}")
	@ResultMap(MapperName.ITEM)
	List<Item> findByUnit(Integer unitId);

	@Select({
		"select * from `item`",
		"inner join `task_item` on `item`.item_id = `task_item`.item_id",
		"inner join `answer` on `item`.item_id = `answer`.item_id",
		"inner join `homework` on `answer`.homework_id = `homework`.homework_id and `homework`.task_id = `task_item`.task_id",
		"where `task_item`.task_id = #{taskId} and `answer`.reviewed is not true",
		"group by `item`.item_id"
	})
	List<Item> findUnreviewedByTask(Integer taskId);

	
	
	
}
