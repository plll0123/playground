plugins {
    id("com.study.common")
    kotlin("jvm")
}

dependencies {
    testImplementation("io.mockk:mockk:1.13.10")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}