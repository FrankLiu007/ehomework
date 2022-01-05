package com.zxrh.ehomework.pojo;

import java.util.List;

public class Answer{

	private Integer answerId;
	
	private String manuscript;
	
	private List<Choice> choices;
	
	private Item item;
	
	private Homework homework;
	
	private Boolean reviewed;
	
	private Review review;
	
	public Integer getAnswerId(){
		return answerId;
	}

	public void setAnswerId(Integer answerId){
		this.answerId = answerId;
	}

	public String getManuscript(){
		return manuscript;
	}

	public void setManuscript(String manuscript){
		this.manuscript = manuscript;
	}

	public List<Choice> getChoices(){
		return choices;
	}

	public void setChoices(List<Choice> choices){
		this.choices = choices;
	}

	public Item getItem(){
		return item;
	}

	public void setItem(Item item){
		this.item = item;
	}

	public Homework getHomework(){
		return homework;
	}

	public void setHomework(Homework homework){
		this.homework = homework;
	}

	public Boolean getReviewed(){
		return reviewed;
	}

	public void setReviewed(Boolean reviewed){
		this.reviewed = reviewed;
	}

	public Review getReview(){
		return review;
	}

	public void setReview(Review review){
		this.review = review;
	}
	
}
