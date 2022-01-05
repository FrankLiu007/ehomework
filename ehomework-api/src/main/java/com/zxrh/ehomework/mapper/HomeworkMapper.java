package com.zxrh.ehomework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;

import com.zxrh.ehomework.common.constant.MapperName;
import com.zxrh.ehomework.pojo.Homework;
import com.zxrh.ehomework.pojo.Subject;
import com.zxrh.ehomework.pojo.Task.Form;

public interface HomeworkMapper{

	@Insert({
		"insert into `homework`",
		"(task_id,student_id,status,remind)",
		"values",
		"(#{task.taskId},#{student.studentId},#{status},#{remind})"
	})
	void insert(Homework homework);
	
	@Select("select * from `homework` where homework_id = #{homeworkId}")
	@Results({
		@Result(column="homework_id",property="homeworkId",id=true),
		@Result(column="student_id",property="student",one=
			@One(select="com.zxrh.ehomework.mapper.StudentMapper.get",fetchType=FetchType.EAGER))
	})
	Homework get(Integer homeworkId);
	
	@Select("select * from `homework` where homework_id = #{homeworkId}")
	@Results({
		@Result(column="homework_id",property="homeworkId",id=true),
		@Result(column="task_id",property="task",one=
			@One(select="com.zxrh.ehomework.mapper.TaskMapper.getDetail",fetchType=FetchType.EAGER)),
		@Result(column="student_id",property="student",one=
			@One(select="com.zxrh.ehomework.mapper.StudentMapper.get",fetchType=FetchType.EAGER))
	})
	Homework getDetail(Integer homeworkId);
	
	@Update({
		"<script>",
			"update `homework`",
			"<set>",
				"<if test='status != null'>",
					"status = #{status},",
				"</if>",
				"<if test='submitted != null'>",
					"submitted = #{submitted},",
				"</if>",
				"<if test='corrected != null'>",
					"corrected = #{corrected},",
				"</if>",
				"<if test='score != null'>",
					"score = #{score},",
				"</if>",
				"<if test='remind != null'>",
					"remind = #{remind}",
				"</if>",
			"</set>",
			"where homework_id = #{homeworkId}",
		"</script>"
	})
	void update(Homework homework);

	@Select("select * from `homework` where task_id = #{taskId} and status > 1")
	@Results(id=MapperName.HOMEWORK,value={
		@Result(column="homework_id",property="homeworkId",id=true),
		@Result(column="student_id",property="student",one=
			@One(select="com.zxrh.ehomework.mapper.StudentMapper.get",fetchType=FetchType.EAGER)),
		@Result(column="task_id",property="task",one=
			@One(select="com.zxrh.ehomework.mapper.TaskMapper.get",fetchType=FetchType.EAGER))
	})
	List<Homework> findSubmittedByTask(Integer taskId);

	@Select({
		"select `homework`.* from `homework`",
		"inner join `answer` using(homework_id)",
		"where `answer`.answer_id = #{answerId}"
	})
	@ResultMap(MapperName.HOMEWORK)
	Homework findByAnswer(Integer answerId);
	
	@Select({
		"<script>",
			"select `homework`.* from `homework`",
			"inner join `task` on `homework`.task_id = `task`.task_id",
			"where `task`.birthline &lt;= now()",
			"<if test='student != null'>",
				"and `homework`.student_id = #{student.studentId}",
			"</if>",
			"<if test='task.form != null'>",
				"and `task`.form = #{task.form}",
			"</if>",
			"<if test='task.course != null'>",
				"and `task`.course_id = #{task.course.courseId}",
			"</if>",
			"<if test='status != null'>",
				"and `homework`.status = #{status.code}",
			"</if>",
		"</script>"
	})
	@ResultMap(MapperName.HOMEWORK)
	List<Homework> findCompliant(Homework homework);

	@Select("select * from `homework` where task_id = #{taskId}")
	@Results({
		@Result(column="homework_id",property="homeworkId",id=true),
		@Result(column="student_id",property="student",one=
			@One(select="com.zxrh.ehomework.mapper.StudentMapper.get",fetchType=FetchType.EAGER)),
		@Result(column="task_id",property="task",one=
			@One(select="com.zxrh.ehomework.mapper.TaskMapper.get",fetchType=FetchType.EAGER)),
		@Result(column="homework_id",property="answers",many=
			@Many(select="com.zxrh.ehomework.mapper.AnswerMapper.findByHomework",fetchType=FetchType.EAGER))
	})
	List<Homework> findByTask(Integer taskId);

	@Select({
		"select (",
		"select count(*) from `answer`",
		"where reviewed is not true",
		"and homework_id = (select homework_id from `answer` where answer_id = #{answerId})",
		") = 0"
	})
	Boolean getReviewedByHomework(Integer answerId);
	
	@Select({
		"select (",
			"select count(*) from `answer`",
			"where homework_id = #{homeworkId}",
			"and reviewed is not true",
		") = 0"
	})
	boolean isActuallyCorrected(Homework homework);
	
	@Select({
		"select sum(`review`.score) from `review`",
		"inner join `answer` using(answer_id)",
		"where `answer`.homework_id = #{homeworkId}"
	})
	Float getSum(Homework homework);

	@Select({
		"select avg(`review`.score) from `review`",
		"inner join `answer` using(answer_id)",
		"where `answer`.homework_id = #{homeworkId}"
	})
	Float getAverage(Homework homework);

	@Select({
		"<script>",
			"select count(*) from `homework`",
			"inner join `task` on `homework`.task_id = `task`.task_id",
			"inner join `course` on `task`.course_id = `course`.course_id",
			"inner join `teacher` on `course`.teacher_id = `teacher`.teacher_id",
			"where `task`.birthline &lt;= now()",
			"and `homework`.remind is true",
			"<if test='studentId != null'>",
				"and `homework`.student_id = #{studentId}",
			"</if>",
			"<if test='form != null'>",
				"and `task`.form = #{form}",
			"</if>",
			"<if test='submitted != null'>",
				"<choose>",
					"<when test='submitted'>",
						"and `homework`.status &gt; 1",
					"</when>",
					"<otherwise>",
						"and `homework`.status = 1",
					"</otherwise>",
				"</choose>",
			"</if>",
			"<if test='subject != null'>",
				"and `teacher`.subject = #{subject}",
			"</if>",
		"</script>"
	})
	int countRemind(Integer studentId, Form form, Boolean submitted, Subject subject);

	@Select({
		"select * from `homework`",
		"where task_id = #{taskId}",
		"and status > 1"
	})
	@ResultMap(MapperName.HOMEWORK)
	List<Homework> reviewByHomework(Integer taskId);
	
}