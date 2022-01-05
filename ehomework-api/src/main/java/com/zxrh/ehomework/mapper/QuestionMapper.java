package com.zxrh.ehomework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import com.zxrh.ehomework.pojo.Question;

public interface QuestionMapper{

	@Options(keyColumn="question_id",keyProperty="questionId",useGeneratedKeys=true)
	@Insert("insert into `question` (number,type,stem,solution,score,item_id) values (#{number},#{type},#{stem},#{solution},#{score},#{item.itemId})")
	void insert(Question question);
	
	@Select("select * from `question` where item_id = #{itemId}")
	@Results({
		@Result(column="question_id",property="questionId",id=true),
		@Result(column="question_id",property="options",many=
			@Many(select="com.zxrh.ehomework.mapper.OptionMapper.findByQuestion",fetchType=FetchType.EAGER))
	})
	List<Question> findByItem(Integer itemId);
	
	@Select("select * from `question` where question_id = #{questionId}")
	Question get(Integer questionId);
	
}