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
import com.zxrh.ehomework.pojo.Course;

public interface CourseMapper{

	@Options(keyColumn="course_id",keyProperty="courseId",useGeneratedKeys=true)
	@Insert("insert into `course` (name,teacher_id) values (#{name},#{teacher.teacherId})")
	void insert(Course course);
	
	@Insert({"<script>",
			"insert into `course_material` (course_id,material_id) values ",
			"<foreach collection='materials' item='material' index='index' separator=','>",
				"(#{courseId},#{material.materialId})",
			"</foreach>",
		"</script>"})
	void addMaterials(Course course);
	
	@Select("select * from `course` where course_id = #{courseId}")
	@Results(id=MapperName.COURSE,value={
		@Result(column="course_id",property="courseId",id=true),
		@Result(column="teacher_id",property="teacher",one=
			@One(select="com.zxrh.ehomework.mapper.TeacherMapper.get",fetchType=FetchType.EAGER)),
		@Result(column="course_id",property="studentNumber",one=
			@One(select="getStudentNumber",fetchType=FetchType.EAGER)),
		@Result(column="course_id",property="taskNumber",one=
			@One(select="getTaskNumber",fetchType=FetchType.EAGER)),
		@Result(column="course_id",property="pending",one=
			@One(select="getPending",fetchType=FetchType.EAGER))
	})
	Course get(Integer courseId);

	@Select("select count(*) from `task` where course_id = #{courseId}")
	Integer getTaskNumber(Integer courseId);
	
	@Select({"select count(*) from `student` ",
		"inner join `mistake_book` on `student`.student_id = `mistake_book`.student_id ",
		"where `mistake_book`.course_id = #{courseId}"})
	Integer getStudentNumber(Integer courseId);
	
	@Select({"select (select count(*) from `homework` ",
		"inner join `task` on `homework`.task_id = `task`.task_id ",
		"where `homework`.status = 2 and `task`.course_id = #{courseId}) > 0"})
	Boolean getPending(Integer courseId);
	
	@Select("select * from `course` where course_id = #{courseId}")
	@Results(id=MapperName.DETAILED_COURSE,value={
		@Result(column="course_id",property="courseId",id=true),
		@Result(column="teacher_id",property="teacher",one=
			@One(select="com.zxrh.ehomework.mapper.TeacherMapper.get",fetchType=FetchType.EAGER)),
		@Result(column="course_id",property="students",many=
			@Many(select="com.zxrh.ehomework.mapper.StudentMapper.findByCourse",fetchType=FetchType.EAGER)),
		@Result(column="course_id",property="tasks",many=
			@Many(select="com.zxrh.ehomework.mapper.TaskMapper.findByCourse",fetchType=FetchType.EAGER)),
		@Result(column="course_id",property="pending",one=
			@One(select="getPending",fetchType=FetchType.EAGER))
	})
	Course getDetail(Integer courseId);
	
	@Select("select * from `course` where teacher_id = #{teacherId}")
	@ResultMap(MapperName.COURSE)
	List<Course> findByTeacher(Integer teacherId);
	
	@Select({
		"select `course`.* from `course` inner join `mistake_book` ",
		"on `course`.course_id = `mistake_book`.course_id ",
		"where `mistake_book`.student_id = #{studentId}"
	})
	@ResultMap(MapperName.COURSE)
	List<Course> findByStudent(Integer studentId);

	@Select("select * from `course`")
	@ResultMap(MapperName.COURSE)
	List<Course> getAll();

}