package com.zxrh.ehomework.common.pojo;

public class FailException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public FailException(String message){
        super(message);
    }
	
}
