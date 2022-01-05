package com.zxrh.ehomework;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan("com.zxrh.ehomework.mapper")
@SpringBootApplication
public class EhomeworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(EhomeworkApplication.class, args);
	}

}
