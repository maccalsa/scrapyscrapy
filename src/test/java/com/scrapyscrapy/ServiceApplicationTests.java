package com.scrapyscrapy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;
import org.testcontainers.utility.TestcontainersConfiguration;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ServiceApplicationTests {

	@Test
	public void contextLoads() {
		var applicationModules = ApplicationModules.of(ScrapyScrapyApplication.class);
		applicationModules.verify();

		System.out.println("applicationModules = " + applicationModules);

		new Documenter(applicationModules).writeModulesAsPlantUml()
			.writeIndividualModulesAsPlantUml()
			.writeDocumentation();
	}

}
