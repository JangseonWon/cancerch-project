plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    id("org.jetbrains.kotlin.plugin.spring") version "1.7.10"
}
dependencies {
    implementation(project(":shared"))
    implementation(libs.bundles.spring.client.without.security)
    implementation("org.apache.pdfbox:pdfbox:2.+")
    implementation("org.jsoup:jsoup:1.+")
    implementation(libs.r2dbc)
    runtimeOnly("io.r2dbc:r2dbc-mssql:0.9.0.RELEASE")
    implementation(libs.bundles.kotlin.webflux)
    testImplementation(libs.bundles.test)
}
configurations { all { exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging") } }
dependencyManagement { imports { mavenBom(libs.spring.cloud.bom.get().toString()) } }

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    processResources {
        if(project.gradle.startParameter.taskNames.contains("build")) exclude("application.yml")
    }
    bootJar {
        archiveFileName.set("avoid-project-alis.jar")
    }
    jar {
        enabled = false
    }
    test {
        useJUnitPlatform()
    }
    clean{
        delete("src/main/resources/static")
        delete("build/")
    }
}
