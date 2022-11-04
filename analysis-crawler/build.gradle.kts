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
    implementation("com.gcgenome:dtoConverter:1.0")
    implementation("com.greencross:jandi-webhook:1.0")
    implementation(libs.bundles.spring.client)
    implementation(libs.bundles.kotlin.webflux)
    implementation(libs.bundles.r2dbc.postgres)
    implementation(libs.bundles.r2dbc.querydsl)
    kapt(libs.bundles.r2dbc.querydsl)
    testImplementation(libs.bundles.test)
}
configurations { all { exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging") } }
dependencyManagement { imports { mavenBom(libs.spring.cloud.bom.get().toString()) } }
kapt {
    keepJavacAnnotationProcessors = true
}
tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    processResources {
        //dependsOn copyWebResources
        if(project.gradle.startParameter.taskNames.contains("build")) exclude("application.yml")
    }
    bootJar {
        archiveFileName.set("avoid-project-analysis-crawler.jar")
    }
    test {
        useJUnitPlatform()
    }
    jar {
        enabled = false
    }
}