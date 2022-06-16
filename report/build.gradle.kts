plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.springframework.boot") version "2.6.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}
configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}
extra["springCloudVersion"] = "2020.0.4"
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.greencross:lims-report:1.0")

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
    implementation("com.infobip:infobip-spring-data-r2dbc-querydsl-boot-starter:7.0.0")
    implementation("io.r2dbc:r2dbc-postgresql")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka") {
        exclude("org.springframework.cloud", "spring-cloud-function-dependencies")
    }
    implementation("org.springframework.cloud:spring-cloud-function-dependencies:3.2.3")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-log4j2") {
        exclude("org.apache.logging.log4j", "log4j-core")
        exclude("org.apache.logging.log4j", "log4j-slf4j-impl")
        exclude("org.apache.logging.log4j", "log4j-jul")
        exclude("org.apache.logging.log4j", "log4j-api")
    }
    implementation("org.apache.logging.log4j:log4j-core:2.17.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.0")
    implementation("org.apache.logging.log4j:log4j-jul:2.17.0")
    implementation("org.apache.logging.log4j:log4j-api:2.17.0")
    implementation("org.apache.pdfbox:pdfbox:2.+")
    implementation("com.google.zxing:core:3.4.0")
    implementation("com.google.zxing:javase:3.4.0")
    implementation("org.springframework.boot:spring-boot-starter-data-cassandra")
    kapt("javax.annotation:javax.annotation-api")
    kapt("com.querydsl:querydsl-core:5.0.0")
    kapt("com.querydsl:querydsl-apt:5.0.0")
    kapt("com.infobip:infobip-spring-data-jdbc-annotation-processor:6.1.3")
    implementation("org.mapstruct:mapstruct:1.+")
    kapt("org.mapstruct:mapstruct-processor:1.+")
}

tasks {
    processResources {
        //dependsOn copyWebResources
        if(project.gradle.startParameter.taskNames.contains("build")) exclude("application.yml")
    }
    jar {
        archiveFileName.set("avoid-service-report.jar")
    }
    clean {
        delete("src/main/resources/static")
        delete("build/")
    }
    test {
        useJUnitPlatform()
    }
}


kapt {
    keepJavacAnnotationProcessors = true
}