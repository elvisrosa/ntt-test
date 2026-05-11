plugins {
	java
	id("org.springframework.boot") version "3.5.14"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.openapi.generator") version "7.2.0"
}

group = "nttdata.test"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.6.0")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("io.swagger.core.v3:swagger-annotations:2.2.20")
	implementation("jakarta.validation:jakarta.validation-api")
	implementation("org.apache.poi:poi:5.2.4")
	implementation("org.apache.poi:poi-ooxml:5.2.4")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.21.2")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:r2dbc-postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
