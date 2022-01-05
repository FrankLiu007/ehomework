package name.wanghwx.ehomework.pojo;

public class Review{

	private Integer id;
	private String postil;
	private String comment;
	private String audio;
	private Float score;
	private Integer answerId;
	
	public Integer getId(){
		return id;
	}
	public void setId(Integer id){
		this.id = id;
	}
	public String getPostil(){
		return postil;
	}
	public void setPostil(String postil){
		this.postil = postil;
	}
	public String getComment(){
		return comment;
	}
	public void setComment(String comment){
		this.comment = comment;
	}
	public String getAudio(){
		return audio;
	}
	public void setAudio(String audio){
		this.audio = audio;
	}
	public Float getScore(){
		return score;
	}
	public void setScore(Float score){
		this.score = score;
	}
	public Integer getAnswerId(){
		return answerId;
	}
	public void setAnswerId(Integer answerId){
		this.answerId = answerId;
	}
	
}