package com.scrapyscrapy;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestScrapyScrapyApplication {

	public static void main(String[] args) {
		SpringApplication.from(ScrapyScrapyApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
