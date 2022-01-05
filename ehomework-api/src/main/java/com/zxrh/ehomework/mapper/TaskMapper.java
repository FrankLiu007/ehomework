package com.zxrh.ehomework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import com.zxrh.ehomework.common.constant.MapperName;
import com.zxrh.ehomework.pojo.Task;

public interface TaskMapper{

	@Options(keyColumn="task_id",keyProperty="taskId",useGeneratedKeys=true)
	@Insert("insert into `task` (form,title,birthline,deadline,remark,course_id) values (#{form},#{title},#{birthline},#{deadline},#{remark},#{course.courseId})")
	void insert(Task task);
	
	@Insert({"<script>",
		"insert into `task_item` (task_id,item_id) values ",
		"<foreach collection='items' item='item' index='index' separator=','>",
			"(#{taskId},#{item.itemId})",
		"</foreach>",
	"</script>"})
	void addItems(Task task);
	
	@Select("select * from `task` where course_id = #{courseId}")
	@Results({
		@Result(column="task_id",property="taskId",id=true),
		@Result(column="task_id",property="homeworkNumber",one=
			@One(select="getHomeworkNumber",fetchType=FetchType.EAGER)),
		@Result(column="task_id",property="submittedNumber",one=
			@One(select="getSubmittedNumber",fetchType=FetchType.EAGER)),
		@Result(column="task_id",property="itemNumber",one=
			@One(select="getItemNumber",fetchType=FetchType.EAGER)),
		@Result(column="task_id",property="reviewedAnswerNumber",one=
			@One(select="getReviewedAnswerNumber",fetchType=FetchType.EAGER))
	})
	List<Task> findByCourse(Integer courseId);
	
	@Select("select count(*) from `homework` where task_id = #{taskId}")
	Integer getHomeworkNumber(Integer taskId);
	@Select("select count(*) from `homework` where task_id = #{taskId} and status > 1")
	Integer getSubmittedNumber(Integer taskId);
	@Select("select count(*) from `task_item` where task_id = #{taskId}")
	Integer getItemNumber(Integer taskId);
	@Select({
		"select count(*) from `answer`",
		"inner join `homework` on `answer`.homework_id = `homework`.homework_id",
		"where `answer`.reviewed is true and `homework`.task_id = #{taskId}"
	})
	Integer getReviewedAnswerNumber(Integer taskId);
	
	@Select("select * from `task` where task_id = #{taskId}")
	@Results(id=MapperName.TASK,value={
		@Result(column="task_id",property="taskId",id=true),
		@Result(column="course_id",property="course",one=
			@One(select="com.zxrh.ehomework.mapper.CourseMapper.get",fetchType=FetchType.EAGER))
	})
	Task get(Integer taskId);
	
	@Select("select * from `task` where task_id = #{taskId}")
	@Results(id=MapperName.DETAILED_TASK,value={
		@Result(column="task_id",property="taskId",id=true),
		@Result(column="course_id",property="course",one=
			@One(select="com.zxrh.ehomework.mapper.CourseMapper.get",fetchType=FetchType.EAGER)),
		@Result(column="task_id",property="items",many=
			@Many(select="com.zxrh.ehomework.mapper.ItemMapper.findByTask",fetchType=FetchType.EAGER))
	})
	Task getDetail(Integer taskId);

	@Select({
		"<script>",
			"select * from `task`",
			"<where>",
				"<if test='course != null'>",
					"and course_id = #{course.courseId}",
				"</if>",
				"<if test='form != null'>",
					"and form = #{form}",
				"</if>",
			"</where>",
		"</script>"
	})
	@Results({
		@Result(column="task_id",property="taskId",id=true),
		@Result(column="task_id",property="homeworkNumber",one=
			@One(select="getHomeworkNumber",fetchType=FetchType.EAGER)),
		@Result(column="task_id",property="submittedNumber",one=
			@One(select="getSubmittedNumber",fetchType=FetchType.EAGER)),
		@Result(column="task_id",property="itemNumber",one=
			@One(select="getItemNumber",fetchType=FetchType.EAGER)),
		@Result(column="task_id",property="reviewedAnswerNumber",one=
			@One(select="getReviewedAnswerNumber",fetchType=FetchType.EAGER))
	})
	List<Task> findCompliant(Task task);
	
}