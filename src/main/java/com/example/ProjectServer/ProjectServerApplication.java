package com.example.ProjectServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectServerApplication {

	public static void main(String[] args) {

		System.setProperty("webdriver.chrome.driver", "src/main/resources/static/chromedriver.exe");
		SpringApplication.run(ProjectServerApplication.class, args);
	}

}
