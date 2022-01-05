package com.zxrh.ehomework.pojo;

import java.util.Date;

public class Material{

	private Integer materialId;
	
	private String name;
	
	private Carrier carrier;
	
	private Subject subject;
	
	private Unit root;
	
	private Date created;

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public Carrier getCarrier(){
		return carrier;
	}

	public void setCarrier(Carrier carrier){
		this.carrier = carrier;
	}

	public Subject getSubject(){
		return subject;
	}

	public void setSubject(Subject subject){
		this.subject = subject;
	}

	public Unit getRoot(){
		return root;
	}

	public void setRoot(Unit root){
		this.root = root;
	}
	
	public Date getCreated(){
		return created;
	}

	public void setCreated(Date created){
		this.created = created;
	}

	public enum Carrier{
		BOOK("参考书",1),PAPER("试卷",2);
		
		private String name;
		private Integer code;
		
		Carrier(String name, Integer code){
			this.name = name;
			this.code = code;
		}

		public String getName(){
			return name;
		}

		public void setName(String name){
			this.name = name;
		}

		public Integer getCode(){
			return code;
		}

		public void setCode(Integer code){
			this.code = code;
		}
		
		public static Carrier getByCode(Integer code){
			for(Carrier carrier:values()){
				if(carrier.code.equals(code)){
					return carrier;
				}
			}
			return null;
		}
		
	}
}