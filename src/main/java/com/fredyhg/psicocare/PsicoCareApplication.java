package com.fredyhg.psicocare;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "PsicoCare API", version = "0.1", description = "Routes documentations"))
public class PsicoCareApplication {

	public static void main(String[] args) {
		SpringApplication.run(PsicoCareApplication.class, args);
	}

}
