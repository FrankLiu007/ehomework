package com.zxrh.ehomework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;

import com.zxrh.ehomework.common.constant.MapperName;
import com.zxrh.ehomework.pojo.Course;
import com.zxrh.ehomework.pojo.MistakeBook;

public interface MistakeBookMapper{

	@Insert({"<script>",
		"insert into `mistake_book` (student_id,course_id) values ",
		"<foreach collection='students' item='student' index='index' separator=','>",
			"(#{student.studentId},#{courseId})",
		"</foreach>",
	"</script>"})
	void insert(Course course);

	@Select("select * from `mistake_book` where student_id = #{studentId} and course_id = #{courseId}")
	@Results(id=MapperName.MISTAKE_BOOK,value={
		@Result(column="mistake_book_id",property="mistakeBookId",id=true),
		@Result(column="student_id",property="student",one=
			@One(select="com.zxrh.ehomework.mapper.StudentMapper.get",fetchType=FetchType.EAGER)),
		@Result(column="course_id",property="course",one=
			@One(select="com.zxrh.ehomework.mapper.CourseMapper.get",fetchType=FetchType.EAGER))})
	MistakeBook find(Integer studentId, Integer courseId);

	@Insert("insert ignore into `mistake_book_item` (mistake_book_id,item_id,remind) values (#{mistakeBookId},#{itemId},true)")
	void collect(Integer mistakeBookId, Integer itemId);

	@Select("select * from `mistake_book` where student_id = #{studentId}")
	@Results({
		@Result(column="mistake_book_id",property="mistakeBookId",id=true),
		@Result(column="course_id",property="course",one=
			@One(select="com.zxrh.ehomework.mapper.CourseMapper.get",fetchType=FetchType.EAGER)),
		@Result(column="mistake_book_id",property="remind",one=
			@One(select="findRemindByMistakeBook",fetchType=FetchType.EAGER))
	})
	List<MistakeBook> findByStudent(Integer studentId);
	
	@Select({
		"select count(*) from `mistake_book_item`",
		"where mistake_book_id = #{mistakeBookId}",
		"and remind is true"
	})
	int findRemindByMistakeBook(Integer mistakeBookId);

	@Select("select * from `mistake_book` where mistake_book_id = #{mistakeBookId}")
	@Results({
		@Result(column="mistake_book_id",property="mistakeBookId",id=true),
		@Result(column="student_id",property="student",one=
			@One(select="com.zxrh.ehomework.mapper.StudentMapper.get",fetchType=FetchType.EAGER)),
		@Result(column="course_id",property="course",one=
			@One(select="com.zxrh.ehomework.mapper.CourseMapper.get",fetchType=FetchType.EAGER))
	})
	MistakeBook getDetail(Integer mistakeBookId);

	@Delete("delete from `mistake_book_item` where mistake_book_id = #{mistakeBookId} and item_id = #{itemId}")
	void abandon(Integer mistakeBookId, Integer itemId);

	@Select({
		"select (",
		"select count(*) from `mistake_book_item`",
		"where mistake_book_id = #{mistakeBookId}",
		"and item_id = #{itemId}",
		") > 0"
	})
	Boolean isCollected(Integer mistakeBookId,Integer itemId);

	@Update({
		"update `mistake_book_item`",
		"set remind = false",
		"where mistake_book_id = #{mistakeBookId}",
		"and item_id = #{itemId}"
	})
	void clearRemind(Integer mistakeBookId, Integer itemId);

}
