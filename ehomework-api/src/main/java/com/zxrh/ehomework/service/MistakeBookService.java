package com.zxrh.ehomework.service;

import java.util.List;

import com.zxrh.ehomework.pojo.MistakeBook;

public interface MistakeBookService{

	MistakeBook find(Integer studentId, Integer courseId);

	void collect(Integer mistakeBookId, Integer itemId);

	List<MistakeBook> findByStudent(Integer studentId);

	MistakeBook getDetail(Integer mistakeBookId);

	void abandon(Integer mistakeBookId, Integer itemId);

	Boolean isCollected(Integer mistakeBookId, Integer studentId);

	void clearRemind(Integer mistakeBookId, Integer itemId);

}