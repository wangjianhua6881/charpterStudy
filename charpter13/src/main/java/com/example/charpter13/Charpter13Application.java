package com.example.charpter13;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PreDestroy;


@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Charpter13Application {

	private  static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		Charpter13Application.context = SpringApplication.run(Charpter13Application.class, args);
	}

	@PreDestroy
	public void close(){
		Charpter13Application.context.close();
	}

}
