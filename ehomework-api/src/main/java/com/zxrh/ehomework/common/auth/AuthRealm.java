package com.zxrh.ehomework.common.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.zxrh.ehomework.common.constant.PreSuf;
import com.zxrh.ehomework.service.AdminService;
import com.zxrh.ehomework.service.RedisService;
import com.zxrh.ehomework.service.StudentService;
import com.zxrh.ehomework.service.TeacherService;

public class AuthRealm extends AuthenticatingRealm{

	@Autowired
	private RedisService redisService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private StudentService studentService;

	@Override
	public boolean supports(AuthenticationToken token){
		return token instanceof AuthToken;
	}
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		String token = (String)authenticationToken.getCredentials();
		String auth = redisService.get(PreSuf.AUTH_KEY_PREFIX+token);
		if(!StringUtils.hasText(auth)) throw new AuthenticationException("Expired Token!");
		if((auth.startsWith(PreSuf.ADMIN_PREFIX) && adminService.get(Integer.valueOf(auth.substring(PreSuf.ADMIN_PREFIX.length()))) != null) ||
				(auth.startsWith(PreSuf.TEACHER_PREFIX) && teacherService.get(Integer.valueOf(auth.substring(PreSuf.TEACHER_PREFIX.length()))) != null) ||
				(auth.startsWith(PreSuf.STUDENT_PREFIX) && studentService.get(Integer.valueOf(auth.substring(PreSuf.STUDENT_PREFIX.length()))) != null)){
			return new SimpleAuthenticationInfo(token,token,"authRealm");
		}else{
			throw new AuthenticationException("Invalid Token!");
		}
	}

}
