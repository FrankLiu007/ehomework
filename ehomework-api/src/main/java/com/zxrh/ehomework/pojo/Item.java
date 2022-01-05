package com.zxrh.ehomework.pojo;

import java.util.List;

import org.springframework.util.CollectionUtils;

public class Item{

	private Integer itemId;
	
	private String category;

	private String title;
	
	private List<Question> questions;
	
	private String reference;
	
	private Float score;
	
	private Unit unit;
	
	private Answer answer;
	
	private Boolean collected;
	
	private boolean remind;

	public Integer getItemId(){
		return itemId;
	}

	public void setItemId(Integer itemId){
		this.itemId = itemId;
	}

	public String getCategory(){
		return category;
	}

	public void setCategory(String category){
		this.category = category;
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public List<Question> getQuestions(){
		return questions;
	}

	public void setQuestions(List<Question> questions){
		this.questions = questions;
	}
	
	public String getReference(){
		return reference;
	}

	public void setReference(String reference){
		this.reference = reference;
	}

	public Unit getUnit(){
		return unit;
	}

	public void setUnit(Unit unit){
		this.unit = unit;
	}

	public Answer getAnswer(){
		return answer;
	}

	public void setAnswer(Answer answer){
		this.answer = answer;
	}
	
	public Boolean isCollected(){
		return collected;
	}
	
	public void setCollected(Boolean collected){
		this.collected = collected;
	}
	
	public boolean isRemind(){
		return remind;
	}

	public void setRemind(boolean remind){
		this.remind = remind;
	}

	public boolean isAllSingle(){
		if(!CollectionUtils.isEmpty(questions)){
			for(Question question:questions){
				if(question.getType() != Question.Type.SINGLE) return false;
			}
			return true;
		}
		return false;
	}

	public Float getScore(){
		return score;
	}

	public void setScore(Float score){
		this.score = score;
	}
	
	public float getTotal(){
		if(CollectionUtils.isEmpty(questions)){
			return 0;
		}else{
			float total = 0;
			for(Question question:questions){
				if(question != null) total += question.getScore();
			}
			return total;
		}
	}
	
}