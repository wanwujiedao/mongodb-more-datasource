package com.dao.mongdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan
//@ComponentScan(basePackages = "com.dao.mongo.dao")
public class MongdbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongdbApplication.class, args);
	}
}
