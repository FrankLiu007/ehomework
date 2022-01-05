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
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;

import com.zxrh.ehomework.common.constant.MapperName;
import com.zxrh.ehomework.pojo.Answer;

public interface AnswerMapper{

	@Select("select * from `answer` where homework_id = #{homeworkId} and item_id = #{itemId}")
	@Results(id=MapperName.DETAILED_ANSWER,value={
		@Result(column="answer_id",property="answerId",id=true),
		@Result(column="item_id",property="item",one=
			@One(select="com.zxrh.ehomework.mapper.ItemMapper.getDetail",fetchType=FetchType.EAGER)),
		@Result(column="homework_id",property="homework",one=
			@One(select="com.zxrh.ehomework.mapper.HomeworkMapper.get",fetchType=FetchType.EAGER)),
		@Result(column="answer_id",property="choices",many=
			@Many(select="com.zxrh.ehomework.mapper.ChoiceMapper.findByAnswer",fetchType=FetchType.EAGER)),
		@Result(column="answer_id",property="review",one=
			@One(select="com.zxrh.ehomework.mapper.ReviewMapper.findByAnswer",fetchType=FetchType.EAGER))
	})
	Answer find(Integer homeworkId,Integer itemId);
	
	@Select("select * from `answer` where answer_id = #{answerId}")
	@ResultMap(MapperName.DETAILED_ANSWER)
	Answer getDetail(Integer answerId);
	
	@Options(keyColumn="answer_id",keyProperty="answerId",useGeneratedKeys=true)
	@Insert("insert into `answer` (manuscript,item_id,homework_id,reviewed) values (#{manuscript},#{item.itemId},#{homework.homeworkId},#{reviewed})")
	void insert(Answer answer);

	@Select({
		"select `answer`.* from `answer`",
		"inner join `task_item` on `answer`.item_id = `task_item`.item_id",
		"inner join `homework` on `answer`.homework_id = `homework`.homework_id and `task_item`.task_id = `homework`.task_id",
		"where `homework`.task_id = #{taskId}",
		"group by `answer`.item_id"
	})
	@Results({
		@Result(column="answer_id",property="answerId",id=true),
		@Result(column="answer_id",property="reviewed",one=
			@One(select="getReviewedByItem",fetchType=FetchType.EAGER)),
		@Result(column="item_id",property="item",one=
			@One(select="com.zxrh.ehomework.mapper.ItemMapper.getDetail",fetchType=FetchType.EAGER))
	})
	List<Answer> reviewByItem(Integer taskId);

	@Select({
		"select (",
		"select count(*) from `answer`",
		"inner join `homework` on `answer`.homework_id = `homework`.homework_id",
		"where `answer`.reviewed is not true",
		"and `answer`.item_id = (select item_id from `answer` where answer_id = #{answerId})",
		"and `homework`.task_id = (select task_id from `homework` where homework_id = (select homework_id from `answer` where answer_id = #{answerId}))",
		") = 0"
	})
	Boolean getReviewedByItem(Integer answerId);
	
	@Select({
		"select `answer`.* from `answer`",
		"inner join `task_item` on `answer`.item_id = `task_item`.item_id",
		"inner join `homework` on `answer`.homework_id = `homework`.homework_id and `task_item`.task_id = `homework`.task_id",
		"where `task_item`.task_id = #{taskId} and `answer`.item_id = #{itemId}"
	})
	@Results({
		@Result(column="answer_id",property="answerId",id=true),
		@Result(column="homework_id",property="homework",one=
			@One(select="com.zxrh.ehomework.mapper.HomeworkMapper.get",fetchType=FetchType.EAGER)),
		@Result(column="answer_id",property="review",one=
			@One(select="com.zxrh.ehomework.mapper.ReviewMapper.findByAnswer",fetchType=FetchType.EAGER))
	})
	List<Answer> reviewItem(Integer taskId, Integer itemId);

	@Select({
		"select `answer`.* from `answer`",
		"inner join `task_item` on `answer`.item_id = `task_item`.item_id",
		"inner join `homework` on `answer`.homework_id = `homework`.homework_id and `task_item`.task_id = `homework`.task_id",
		"where `task_item`.task_id = #{taskId} and `answer`.homework_id = #{homeworkId}"
	})
	@Results({
		@Result(column="answer_id",property="answerId",id=true),
		@Result(column="item_id",property="item",one=
			@One(select="com.zxrh.ehomework.mapper.ItemMapper.getDetail",fetchType=FetchType.EAGER)),
		@Result(column="answer_id",property="review",one=
			@One(select="com.zxrh.ehomework.mapper.ReviewMapper.findByAnswer",fetchType=FetchType.EAGER))
	})
	List<Answer> reviewHomework(Integer taskId, Integer homeworkId);

	@Select("select * from `answer` where answer_id = #{answerId}")
	Answer get(Integer answerId);

	@Update({
		"update `answer`",
		"set reviewed = true",
		"where answer_id = #{answerId}"
	})
	void review(Integer answerId);

	@Select("select * from `answer` where homework_id = #{homeworkId}")
	@Results({
		@Result(column="answer_id",property="answerId",id=true),
		@Result(column="item_id",property="item",one=
			@One(select="com.zxrh.ehomework.mapper.ItemMapper.getDetail",fetchType=FetchType.EAGER)),
		@Result(column="answer_id",property="review",one=
			@One(select="com.zxrh.ehomework.mapper.ReviewMapper.findByAnswer",fetchType=FetchType.EAGER))
	})
	List<Answer> findByHomework(Integer homeworkId);

	@Select({
		"select `answer`.* from `answer`",
		"inner join `homework` using(homework_id)",
		"where `answer`.item_id = #{itemId}",
		"and `homework`.task_id = #{taskId}"
	})
	@ResultMap(MapperName.DETAILED_ANSWER)
	List<Answer> findByTask(Integer taskId, Integer itemId);
	
}
