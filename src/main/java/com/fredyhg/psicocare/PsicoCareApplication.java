package com.fredyhg.psicocare;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "PsicoCare API", version = "0.1", description = "Routes documentations"))
public class PsicoCareApplication {

	public static void main(String[] args) {
		SpringApplication.run(PsicoCareApplication.class, args);
	}

}
