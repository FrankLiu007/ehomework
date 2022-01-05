package com.zxrh.ehomework.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zxrh.ehomework.pojo.Review;

public interface ReviewMapper{

	@Select("select * from `review` where answer_id = #{answerId}")
	Review findByAnswer(Integer answerId);

	@Insert("insert into `review` (postil,comment,audio,audio_duration,score,answer_id) values (#{postil},#{comment},#{audio},#{audioDuration},#{score},#{answerId})")
	void insert(Review review);

	@Update("update `review` set postil=#{postil},comment=#{comment},audio=#{audio},score=#{score} where answer_id = #{answerId}")
	void update(Review review);

}