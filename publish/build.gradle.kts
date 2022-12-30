plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    id("org.jetbrains.kotlin.plugin.spring") version "1.7.20"
}
dependencies {
    implementation(project(":shared"))
    implementation(project(":search"))
    implementation(libs.bundles.spring.client)
    implementation("com.greencross:jandi-webhook:1.0")
    implementation("org.apache.pdfbox:pdfbox:2.+")
    implementation("org.springframework.boot:spring-boot-starter-data-cassandra")
    implementation(libs.bundles.kotlin.webflux)
    implementation(libs.bundles.r2dbc.postgres)
    implementation(libs.bundles.r2dbc.querydsl)
    implementation("com.gcgenome:alis-report-api:1.0.0-SNAPSHOT")
    kapt(libs.bundles.r2dbc.querydsl)
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
        archiveFileName.set("avoid-project-publish.jar")
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
