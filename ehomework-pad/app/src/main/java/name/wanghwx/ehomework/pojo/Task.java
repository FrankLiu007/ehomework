package name.wanghwx.ehomework.pojo;

import java.util.Date;
import java.util.List;

public class Task{

	private Integer taskId;
	
	private Form form;
	
	private String title;
	
	private Date birthline;
	
	private Date deadline;
	
	private String remark;
	
	private Course course;
	
	private List<Item> items;
	
	private List<Homework> homeworks;

	private Integer homeworkNumber;
	
	private Integer submittedNumber;
	
	private Integer itemNumber;
	
	private Integer reviewedAnswerNumber;
	
	public Integer getTaskId(){
		return taskId;
	}

	public void setTaskId(Integer taskId){
		this.taskId = taskId;
	}

	public Form getForm(){
		return form;
	}

	public void setForm(Form form){
		this.form = form;
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public Date getBirthline(){
		return birthline;
	}

	public void setBirthline(Date birthline){
		this.birthline = birthline;
	}

	public Date getDeadline(){
		return deadline;
	}

	public void setDeadline(Date deadline){
		this.deadline = deadline;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public Course getCourse(){
		return course;
	}

	public void setCourse(Course course){
		this.course = course;
	}

	public List<Item> getItems(){
		return items;
	}

	public void setItems(List<Item> items){
		this.items = items;
	}

	public List<Homework> getHomeworks(){
		return homeworks;
	}

	public void setHomeworks(List<Homework> homeworks){
		this.homeworks = homeworks;
	}

	public Integer getHomeworkNumber(){
		return homeworkNumber;
	}

	public void setHomeworkNumber(Integer homeworkNumber){
		this.homeworkNumber = homeworkNumber;
	}

	public Integer getSubmittedNumber(){
		return submittedNumber;
	}

	public void setSubmittedNumber(Integer submittedNumber){
		this.submittedNumber = submittedNumber;
	}

	public Integer getItemNumber(){
		return itemNumber;
	}

	public void setItemNumber(Integer itemNumber){
		this.itemNumber = itemNumber;
	}

	public Integer getReviewedAnswerNumber(){
		return reviewedAnswerNumber;
	}

	public void setReviewedAnswerNumber(Integer reviewedAnswerNumber){
		this.reviewedAnswerNumber = reviewedAnswerNumber;
	}

	public enum Form{
		ASSIGNMENT(1,"作业"),TEST(2,"考试");
		private Integer code;
		private String message;
		private Form(Integer code,String message){
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
		public static Form getByCode(Integer code){
			for(Form form:values()){
				if(form.code == code){
					return form;
				}
			}
			return null;
		}
	}
	
}