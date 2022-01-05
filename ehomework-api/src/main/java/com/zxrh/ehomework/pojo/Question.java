package com.zxrh.ehomework.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Question{

	private Integer questionId;
	
	private String number;
	
	private Type type;
	
	private String stem;
	
	private List<Option> options;
	
	private String solution;
	
	private Float score;
	
	private List<Choice> choices;
	
	@JsonIgnore
	private Item item;

	public Integer getQuestionId(){
		return questionId;
	}

	public void setQuestionId(Integer questionId){
		this.questionId = questionId;
	}

	public String getNumber(){
		return number;
	}

	public void setNumber(String number){
		this.number = number;
	}

	public Type getType(){
		return type;
	}

	public void setType(Type type){
		this.type = type;
	}

	public Item getItem(){
		return item;
	}

	public void setItem(Item item){
		this.item = item;
	}

	public List<Option> getOptions(){
		return options;
	}

	public void setOptions(List<Option> options){
		this.options = options;
	}

	public String getSolution(){
		return solution;
	}

	public void setSolution(String solution){
		this.solution = solution;
	}

	public Float getScore(){
		return score==null?0:score;
	}

	public void setScore(Float score){
		this.score = score;
	}

	public String getStem(){
		return stem;
	}

	public void setStem(String stem){
		this.stem = stem;
	}

	public List<Choice> getChoices(){
		return choices;
	}

	public void setChoices(List<Choice> choices){
		this.choices = choices;
	}

	public enum Type{
		SINGLE(1,"单选题"),MULTIPLE(2,"多选题"),GENERAL(3,"普通题");
		
		private Integer code;
		private String message;
		
		Type(Integer code,String message){
			this.code = code;
			this.message = message;
		}

		public Integer getCode(){
			return code;
		}

		public void setCode(Integer code){
			this.code = code;
		}

		public String getMessage(){
			return message;
		}

		public void setMessage(String message){
			this.message = message;
		}
		
		public static Type getByCode(Integer code){
			for(Type type:values()){
				if(type.code == code){
					return type;
				}
			}
			return null;
		}
		
	}
}
