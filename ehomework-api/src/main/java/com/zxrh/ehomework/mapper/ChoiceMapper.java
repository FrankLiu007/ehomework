package com.zxrh.ehomework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import com.zxrh.ehomework.common.constant.MapperName;
import com.zxrh.ehomework.pojo.Answer;
import com.zxrh.ehomework.pojo.Choice;

public interface ChoiceMapper{

	@Insert({"<script>",
		"insert into `choice` (content,question_id,answer_id) values ",
		"<foreach collection='choices' item='choice' index='index' separator=','>",
			"(#{choice.content},#{choice.question.questionId},#{answerId})",
		"</foreach>",
	"</script>"})
	void insert(Answer answer);
	
	@Select("select * from `choice` where answer_id = #{answerId}")
	@Results(id=MapperName.DETAILED_CHOICE,value={
		@Result(column="choice_id",property="choiceId",id=true),
		@Result(column="question_id",property="question",one=
			@One(select="com.zxrh.ehomework.mapper.QuestionMapper.get",fetchType=FetchType.EAGER)),
	})
	List<Choice> findByAnswer(Integer answerId);

}
