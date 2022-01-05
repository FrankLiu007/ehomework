package name.wanghwx.ehomework.pojo;

public class Choice{

	private Integer choiceId;
	
	private String content;
	
	private Answer answer;
	
	private Question question;

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
	
}
