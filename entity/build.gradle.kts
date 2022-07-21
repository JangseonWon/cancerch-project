plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    id("org.jetbrains.kotlin.plugin.spring") version "1.7.10"
}
dependencies {
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.kotlin.webflux)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
}
kapt {
    keepJavacAnnotationProcessors = true
    includeCompileClasspath = false
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}