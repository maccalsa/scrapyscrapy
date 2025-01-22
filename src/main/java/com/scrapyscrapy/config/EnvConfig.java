package com.scrapyscrapy.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class EnvConfig {

	private List<String> envKeys = List.of("SECURITY_USERNAME", "SECURITY_PASSWORD", "DB_USERNAME", "DB_PASSWORD",
			"DB_PORT", "DB_HOST", "DB_NAME", "JWT_SECRET", "JWT_EXPIRATION");

	@PostConstruct
	public void loadEnv() {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().stream().filter(e -> envKeys.contains(e.getKey())).forEach(e -> {
			System.setProperty(e.getKey(), e.getValue());
			if (log.isInfoEnabled()) {
				log.info("Loaded environment variable: {} = {}", e.getKey(), e.getValue());
			}
		});
	}

}