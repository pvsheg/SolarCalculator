package com.fullstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringBootHelloWorldApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringBootHelloWorldApplication.class, args);
		TempProcessor processor = new TempProcessor();
		processor.generateMaps();
		
	}
}
