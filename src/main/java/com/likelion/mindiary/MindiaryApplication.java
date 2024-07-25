package com.likelion.mindiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.likelion.mindiary")
public class MindiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindiaryApplication.class, args);
	}

}
