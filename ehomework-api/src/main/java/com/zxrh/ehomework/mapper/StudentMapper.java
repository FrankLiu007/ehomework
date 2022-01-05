package com.zxrh.ehomework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import com.zxrh.ehomework.common.constant.MapperName;
import com.zxrh.ehomework.pojo.Homework;
import com.zxrh.ehomework.pojo.Student;
import com.zxrh.ehomework.pojo.Subject;
import com.zxrh.ehomework.pojo.Task.Form;

public interface StudentMapper{

	@Insert("insert into `student` (account,password,name) values (#{account},#{password},#{name})")
	void insert(Student student);

	@Select({
		"<script>",
			"select * from `student`",
			"<if test='list != null'>",
				"where student_id in",
				"<foreach collection='list' item='studentId' index='index' open='(' separator=',' close=')'>",
					"#{studentId}",
				"</foreach>",
			"</if>",
		"</script>"
	})
	List<Student> getAll(List<Integer> studentIds);
	
	@Select({
		"select * from `student` inner join `mistake_book`",
		"on `student`.student_id = `mistake_book`.student_id",
		"where `mistake_book`.course_id = #{courseId}"
	})
	List<Student> findByCourse(Integer courseId);

	@Select("select * from `student` where student_id = #{studentId}")
	Student get(Integer studentId);

	@Select({
		"<script>",
			"select * from `homework`",
			"inner join `task` on `homework`.task_id = `task`.task_id",
			"where `homework`.student_id = #{student.studentId}",
			"and `task`.birthline &lt;= now()",
			"<if test='status != null'>",
				"and `homework`.status = #{status.code}",
			"</if>",
			"<if test='task.form != null'>",
				"and `task`.form = #{task.form}",
			"</if>",
			"order by `task`.birthline desc",
		"</script>"
	})
	@ResultMap(MapperName.HOMEWORK)
	List<Homework> findAll(Homework homework);
	
	@Select({"<script>",
		"select count(*) from `homework` inner join `task`",
		"on `homework`.task_id = `task`.task_id",
		"where `homework`.student_id = #{student.studentId}",
		"and `task`.birthline &lt;= now()",
		"<if test='status != null'>",
			"and `homework`.status = #{status.code}",
		"</if>",
		"<if test='task.form != null'>",
			"and `task`.form = #{task.form}",
		"</if>",
	"</script>"})
	Integer countHomework(Homework homework);

	@Select({
		"select `homework`.* from `homework` inner join `task` ",
		"on `homework`.task_id = `task`.task_id ",
		"where `task`.course_id = #{courseId} and `homework`.student_id = #{studentId}"
	})
	@Results(id=MapperName.HOMEWORK,value={
		@Result(column="homework_id",property="homeworkId",id=true),
		@Result(column="student_id",property="student",one=
			@One(select="com.zxrh.ehomework.mapper.StudentMapper.get",fetchType=FetchType.EAGER)),
		@Result(column="task_id",property="task",one=
			@One(select="com.zxrh.ehomework.mapper.TaskMapper.get",fetchType=FetchType.EAGER))
	})
	List<Homework> findHomeworksByCourse(Integer courseId, Integer studentId);

	@Select("select * from `student` where account = #{account}")
	Student findByAccount(String account);

	@Select({
		"<script>",
			"select `homework`.* from `homework`",
			"inner join `task` on `homework`.task_id = `task`.task_id",
			"inner join `course` on `task`.course_id = `course`.course_id",
			"inner join `teacher` on `course`.teacher_id = `teacher`.teacher_id",
			"where `task`.birthline &lt;= now()",
			"<if test='student != null'>",
				"and `homework`.student_id = #{student.studentId}",
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
			"order by `task`.birthline desc",
		"</script>"
	})
	@ResultMap(MapperName.HOMEWORK)
	List<Homework> find(Student student,Form form, Boolean submitted, Subject subject);

	@Select({
		"select (",
			"select count(*) from `homework`",
			"inner join `task` on `homework`.task_id = `task`.task_id",
			"where `task`.form = 1",
			"and `task`.birthline <= now()",
			"and `homework`.remind is true",
			"and `homework`.student_id = #{studentId}",
		") > 0"
	})
	boolean findHomeworkRemind(Student student);

	@Select({
		"select (",
			"select count(*) from `homework`",
			"inner join `task` on `homework`.task_id = `task`.task_id",
			"where `task`.form = 2",
			"and `task`.birthline <= now()",
			"and `homework`.remind is true",
			"and `homework`.student_id = #{studentId}",
		") > 0"
	})
	boolean findTestRemind(Student student);

	@Select({
		"select (",
			"select count(*) from `mistake_book_item`",
			"inner join `mistake_book` on `mistake_book_item`.mistake_book_id = `mistake_book`.mistake_book_id",
			"where `mistake_book_item`.remind is true",
			"and `mistake_book`.student_id = #{studentId}",
		") > 0"
	})
	boolean findMistakeRemind(Student student);

}
