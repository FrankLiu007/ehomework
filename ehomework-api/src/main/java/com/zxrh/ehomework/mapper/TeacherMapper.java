package com.zxrh.ehomework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zxrh.ehomework.pojo.Teacher;

public interface TeacherMapper{

	@Insert("insert into `teacher` (account,password,name,subject,avatar,phone) values (#{account},#{password},#{name},#{subject},#{avatar},#{phone})")
	void insert(Teacher teacher);
	
	@Select("select * from `teacher` where teacher_id = #{teacherId}")
	Teacher get(Integer teacherId);

	@Select("select * from `teacher` where account = #{account}")
	Teacher findByAccount(String account);

	@Select("select * from `teacher`")
	List<Teacher> getAll();

	@Select({
		"<script>",
			"select * from `teacher`",
			"<where>",
				"<if test='keyword != null and keyword.length() &gt; 0'>",
					"account like '%${keyword}%'",
					"or name like '%${keyword}%'",
					"or phone like '%${keyword}%'",
				"</if>",
			"</where>",
		"</script>"
	})
	List<Teacher> search(String keyword);

	@Update({
		"<script>",
			"update `teacher`",
			"<set>",
				"<if test='name != null'>",
					"name = #{name},",
				"</if>",
				"<if test='avatar != null'>",
					"avatar = #{avatar}",
				"</if>",
			"</set>",
			"where teacher_id = #{teacherId}",
		"</script>"
	})
	void update(Teacher teacher);
	
}