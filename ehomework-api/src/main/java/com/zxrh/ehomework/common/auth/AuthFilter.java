package com.zxrh.ehomework.common.auth;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxrh.ehomework.common.constant.MapKey;
import com.zxrh.ehomework.common.pojo.Result;
import com.zxrh.ehomework.common.properties.ShiroProperties;

@Component
public class AuthFilter extends BasicHttpAuthenticationFilter{
	
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ShiroProperties shiroProperties;
	
	private AntPathMatcher pathMatcher = new AntPathMatcher();
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		String uri = WebUtils.toHttp(request).getRequestURI();
		List<String> publicUrls = shiroProperties.getPublicUrls();
		for(String publicUrl:publicUrls){
			if(pathMatcher.match(publicUrl,uri)) return true;
		}
		return false;
	}
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		return isLoginAttempt(request,response) && executeLogin(request,response);
	}
	
	@Override
	protected boolean isLoginAttempt(ServletRequest servletRequest, ServletResponse servletResponse){
		boolean withToken = StringUtils.hasText(getToken(servletRequest));
		if(!withToken){
			Result<Object> result = new Result<>().code(2).message("Missing Token!");
			try{
				writeResponse(servletResponse,HttpStatus.OK,result);
			}catch(IOException ignore){}
		}
		return withToken;
	}
	
	@Override
	protected boolean executeLogin(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		AuthToken jwtToken = new AuthToken(getToken(servletRequest));
		try{
			getSubject(servletRequest,servletResponse).login(jwtToken);
			return true;
		}catch (AuthenticationException e){
			Result<Object> response = new Result<>().code(3).message(e.getMessage());
			writeResponse(servletResponse,HttpStatus.OK,response);
            return false;
		}
	}
	
	private String getToken(ServletRequest request){
		HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		return httpServletRequest.getHeader(MapKey.AUTHENTICATION);
	}
	
	private void writeResponse(ServletResponse servletResponse,HttpStatus httpStatus,Result<Object> result) throws IOException{
		HttpServletResponse httpServletResponse = WebUtils.toHttp(servletResponse);
		httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("content-type","text/html;charset=UTF-8");
        httpServletResponse.setStatus(httpStatus.value());
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(result));
	}
	
	@Override
	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		return super.onPreHandle(request, response, mappedValue);
	}
	
  	@Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
  		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个 option请求，这里我们给 option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
	
}