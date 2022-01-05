package com.zxrh.ehomework.pojo;

public class Choice{

	private Integer choiceId;
	
	private String content;
	
	private Answer answer;
	
	private Question question;

	private Integer count;
	
	public Integer getChoiceId(){
		return choiceId;
	}

	public void setChoiceId(Integer choiceId){
		this.choiceId = choiceId;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public Answer getAnswer(){
		return answer;
	}

	public void setAnswer(Answer answer){
		this.answer = answer;
	}

	public Question getQuestion(){
		return question;
	}

	public void setQuestion(Question question){
		this.question = question;
	}

	public Integer getCount(){
		return count;
	}

	public void setCount(Integer count){
		this.count = count;
	}
	
	public boolean isRight(){
		return question != null && question.getSolution() != null && question.getSolution().equals(content);
	}
	
}