package com.zxrh.ehomework.common.config;

import java.util.HashMap;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zxrh.ehomework.common.auth.AuthFilter;
import com.zxrh.ehomework.common.auth.AuthRealm;

@Configuration
public class ShiroConfig{
	
	@Bean
	public FilterRegistrationBean<? extends Filter> authFilterBean(AuthFilter authFilter){
		FilterRegistrationBean<AuthFilter> filterRegistrationBean = new FilterRegistrationBean<>(authFilter);
		filterRegistrationBean.setEnabled(false);
		return filterRegistrationBean;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,AuthFilter authFilter){
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		HashMap<String,Filter> filters = new HashMap<>();
		filters.put("auth",authFilter);
		shiroFilterFactoryBean.setFilters(filters);
		HashMap<String,String> filterChainDefinitionMap = new HashMap<>();
		filterChainDefinitionMap.put("/**","auth");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}
	
	@Bean
	public Realm realm(){
		return new AuthRealm();
	}
	
}
