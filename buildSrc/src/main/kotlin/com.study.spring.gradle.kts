plugins {
    id("com.study.kotlin")
    id("io.spring.dependency-management")
    id("org.springframework.boot")
    kotlin("kapt")
    listOf("noarg", "allopen", "spring", "jpa").forEach {
        kotlin("plugin.$it")
    }
}

allOpen {
    listOf("Entity", "MappedSuperclass", "Embeddable").forEach {
        annotation("jakarta.persistence.$it")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.rest-assured:rest-assured")
}
