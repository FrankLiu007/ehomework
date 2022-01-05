package com.zxrh.ehomework.common.auth;

import java.util.Calendar;

import org.apache.shiro.authc.AuthenticationToken;

public class AuthToken implements AuthenticationToken{

	private static final long serialVersionUID = 1L;

	private String token;
	
	private Calendar expireAt = Calendar.getInstance();
	
	public AuthToken(String token){
		this.token = token;
	}
	
	public AuthToken(String token,Calendar calendar){
		this.token = token;
		this.expireAt.setTime(calendar.getTime());
	}
	
	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}
	
}
