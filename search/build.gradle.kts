plugins {
    kotlin("jvm")
    id("java")
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    id("org.jetbrains.kotlin.plugin.spring") version "1.7.10"
}
dependencies {
    implementation(libs.kotlin.jackson)
    implementation(libs.webflux)
    implementation(libs.r2dbc)
    implementation(libs.querydsl.core)
    implementation(libs.querydsl.r2dbc)
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}
tasks.test {
    useJUnitPlatform()
}