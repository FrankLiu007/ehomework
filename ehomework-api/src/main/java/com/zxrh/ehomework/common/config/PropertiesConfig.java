package com.zxrh.ehomework.common.config;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import com.zxrh.ehomework.common.properties.OSSProperties;
import com.zxrh.ehomework.common.properties.ShiroProperties;

@Configuration
public class PropertiesConfig{

	@Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        factory.setMaxRequestSize(DataSize.ofMegabytes(20));
        return factory.createMultipartConfig();
    }
	
	@Bean
	@ConfigurationProperties(prefix="properties.shiro")
	public ShiroProperties shiroProperties(){
		return new ShiroProperties();
	}
	
	@Bean
	@ConfigurationProperties(prefix="properties.oss")
	public OSSProperties ossProperties(){
		return new OSSProperties();
	}
	
}