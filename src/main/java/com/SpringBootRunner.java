package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.dao")
//@ComponentScan(basePackages = "com")
public class SpringBootRunner {
	
	public static void main (String[] args) {
		SpringApplication.run(SpringBootRunner.class, args);
	}

}
