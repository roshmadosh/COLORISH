plugins {
    java
	id("org.springframework.boot") version "2.7.8"
}

apply(plugin = "io.spring.dependency-management")

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
	testImplementation("org.assertj:assertj-core:3.23.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// spring boot
	implementation("org.springframework.boot:spring-boot-starter-web")

	/* implementation("org.springframework.boot:spring-boot-starter-data-jpa") */
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
