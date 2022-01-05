package name.wanghwx.ehomework.pojo;

import java.util.Date;
import java.util.List;

public class Homework{

	private Integer homeworkId;
	
	private Task task;
	
	private Student student;
	
	private List<Answer> answers;

	private Status status;
	
	private Date submitted;
	
	private Date corrected;
	
	private Float score;

	private boolean remind;

	public Integer getHomeworkId(){
		return homeworkId;
	}

	public void setHomeworkId(Integer homeworkId){
		this.homeworkId = homeworkId;
	}

	public Task getTask(){
		return task;
	}

	public void setTask(Task task){
		this.task = task;
	}

	public Student getStudent(){
		return student;
	}

	public void setStudent(Student student){
		this.student = student;
	}
	
	public List<Answer> getAnswers(){
		return answers;
	}

	public void setAnswers(List<Answer> answers){
		this.answers = answers;
	}

	public Status getStatus(){
		return status;
	}

	public void setStatus(Status status){
		this.status = status;
	}

	public Date getSubmitted(){
		return submitted;
	}

	public void setSubmitted(Date submitted){
		this.submitted = submitted;
	}

	public Date getCorrected(){
		return corrected;
	}

	public void setCorrected(Date corrected){
		this.corrected = corrected;
	}

	public Float getScore(){
		return score;
	}

	public void setScore(Float score){
		this.score = score;
	}

	public boolean isRemind() {
		return remind;
	}

	public void setRemind(boolean remind) {
		this.remind = remind;
	}

	public enum Status{
		UNSUBMITTED(1,"待提交"),UNCORRECTED(2,"待批阅"),CORRECTED(3,"已批阅");
		
		Status(Integer code,String message){
			this.code = code;
			this.message = message;
		}
		
		private Integer code;
		
		private String message;

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
		
		public static Status getByCode(Integer code){
			for(Status status:values()){
				if(status.code.equals(code)){
					return status;
				}
			}
			return null;
		}
	}
}
