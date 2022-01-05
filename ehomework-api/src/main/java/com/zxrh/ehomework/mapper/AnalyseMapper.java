package com.zxrh.ehomework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.zxrh.ehomework.pojo.Choice;

public interface AnalyseMapper{

	@Select({
		"select avg(`review`.score) from `review`",
		"inner join `answer` on `review`.answer_id = `answer`.answer_id",
		"inner join `task_item` on `answer`.item_id = `task_item`.item_id",
		"inner join `homework` on `answer`.homework_id = `homework`.homework_id and `task_item`.task_id = `homework`.task_id",
		"where `task_item`.task_id = #{taskId} and `task_item`.item_id = #{itemId}"
	})
	Float getAverageScore(Integer taskId, Integer itemId);

	@Select({
		"select `choice`.*,count(*) as count from `choice`",
		"inner join `answer` on `choice`.answer_id = `answer`.answer_id",
		"inner join `homework` on `answer`.homework_id = `homework`.homework_id",
		"where `homework`.task_id = #{taskId} and `choice`.question_id = #{questionId}",
		"group by `choice`.content"
	})
	List<Choice> getGroupedChoices(Integer taskId, Integer questionId);

}