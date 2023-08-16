package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.dao")
@ComponentScan(basePackages = "com")
public class SpringBootRunner {
	
	public static void main (String[] args) {
		SpringApplication.run(SpringBootRunner.class, args);
	}

}
