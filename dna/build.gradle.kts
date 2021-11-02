plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

version = "1.0"

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

extra["springCloudVersion"] = "2020.0.3"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.greencross:lims-api-gateway-data:1.0")
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.springframework.cloud:spring-cloud-starter-zookeeper-discovery")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("com.querydsl:querydsl-core:5.0.0")
    implementation("com.querydsl:querydsl-apt:5.0.0")
    implementation("com.infobip:infobip-spring-data-r2dbc-querydsl-boot-starter:6.1.3")
    runtimeOnly("io.r2dbc:r2dbc-postgresql")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    kapt("javax.annotation:javax.annotation-api")
    kapt("com.querydsl:querydsl-core:5.0.0")
    kapt("com.querydsl:querydsl-apt:5.0.0")
    kapt("com.infobip:infobip-spring-data-jdbc-annotation-processor:6.1.3")
    implementation("org.mapstruct:mapstruct:1.+")
    kapt("org.mapstruct:mapstruct-processor:1.+")
}

tasks.register<Copy>("copyWebResources") {
    delete(files("src/main/resources/static"))
    dependsOn(":dna-ui:build")
    from(zipTree("../worklist-ui/build/libs/RnD-dna.war")) {
        include("**/*.js")
        include("**/*.css")
        include("**/*.png")
        include("**/*.gif")
        include("**/*.svg")
        include("**/*.ttf")
        include("**/*.woff")
        include("**/*.woff2")
        include("**/*.eot")
        include("*.ico")
        include("*.html")
        includeEmptyDirs = false
    }
    into("src/main/resources/static")
}

kapt {
    includeCompileClasspath = false
}

tasks.processResources {
    dependsOn("copyWebResources")
    if(project.gradle.startParameter.taskNames.contains("build")) exclude("application.yml")
}
tasks {
    bootJar {
        archiveFileName.set("RnD-dna.jar")
    }
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        javaParameters = true
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=compatibility")
    }
}
tasks.getByName<Jar>("jar") {
    enabled = false
}
tasks.withType<Delete> {
    doFirst{
        delete("src/main/resources/static")
        delete("build/")
    }
}