package name.wanghwx.ehomework.pojo;

public class Teacher{

	private Integer teacherId;
	
	private String account;
	
	private String password;
	
	private String name;
	
	private Subject subject;
	
	private String avatar;
	
	private String phone;
	
	public Integer getTeacherId(){
		return teacherId;
	}

	public void setTeacherId(Integer teacherId){
		this.teacherId = teacherId;
	}

	public String getAccount(){
		return account;
	}

	public void setAccount(String account){
		this.account = account;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public Subject getSubject(){
		return subject;
	}

	public void setSubject(Subject subject){
		this.subject = subject;
	}

	public String getAvatar(){
		return avatar;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getPhone(){
		return phone;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}
	
	
	
}
