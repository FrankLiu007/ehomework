package com.zxrh.ehomework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.zxrh.ehomework.pojo.Option;
import com.zxrh.ehomework.pojo.Question;

public interface OptionMapper{

	@Insert({"<script>",
		"insert into `option` (label,content,question_id) values ",
		"<foreach collection='options' item='option' index='index' separator=','>",
			"(#{option.label},#{option.content},#{questionId})",
		"</foreach>",
	"</script>"})
	void insert(Question question);
	
	@Select("select * from `option` where question_id = #{questionId}")
	List<Option> findByQuestion(Integer questionId);
	
}
