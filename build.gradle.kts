import java.util.*

val springdocVersion = "2.2.0"
val jjwtVersion = "0.11.5"


plugins {
	java
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	id("application")
//	id("org.sonarqube") version "4.4.1.3373"
	id("jacoco")
}

application {
	mainClass = "com.fredyhg.psicocare.PsicoCareApplication"
}

jacoco {
	toolVersion = "0.8.11"
}

group = "com.fredyhg"
version = "1.0"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocVersion")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("com.fasterxml.jackson.core:jackson-databind")
	implementation("org.flywaydb:flyway-core")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
	annotationProcessor("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")
	compileOnly("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootBuildImage {
	builder.set("paketobuildpacks/builder-jammy-base:latest")
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required = true
	}
}

val properties = Properties()
file("local.properties").inputStream().use {
	properties.load(it)
}
val sonarKey = properties.getProperty("SONAR_KEY")


//sonarqube {
//	properties {
//		property("sonar.projectKey", "psicocare")
//		property("sonar.host.url", "http://localhost:9000")
//		property("sonar.login", sonarKey)
//		property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
//		property("sonar.exclusions", "**/models/**")
//		property("sonar.coverage.exclusions", "**/exceptions/**, **/dtos/**,PsicoCareApplication.java, **/controllers/**, **/security/configs/**")
//	}
//}