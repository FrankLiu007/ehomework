package com.zxrh.ehomework.service;

import com.zxrh.ehomework.pojo.Review;

public interface ReviewService{

	Review findByAnswer(Integer answerId);

	void insert(Review review);

	void update(Review review);

}