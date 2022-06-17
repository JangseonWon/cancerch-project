plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jetbrains.kotlin.plugin.spring") version "1.6.21"
}
dependencyManagement { imports { mavenBom(libs.spring.cloud.bom.get().toString()) } }
dependencies {
    implementation(project(":shared"))
    implementation(project(":search"))
    implementation("com.greencross:lims-api-gateway-data:1.0")
    implementation("org.springframework.cloud:spring-cloud-starter-zookeeper-discovery")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    implementation(libs.bundles.kotlin.webflux)
    implementation(libs.bundles.r2dbc.postgres)
    implementation(libs.bundles.r2dbc.querydsl)
    kapt(libs.bundles.r2dbc.querydsl)
    testImplementation(libs.bundles.test)
}
configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        exclude(group = "org.eclipse.jetty", module = "jetty-server")
    }
}
kapt {
    keepJavacAnnotationProcessors = true
}
tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    register<Copy>("copyWebResources") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        delete(files("src/main/resources/static"))
        dependsOn(":worklist-ui:build")
        from(zipTree("../worklist-ui/build/libs/worklist-ui.war")) {
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
    processResources {
        dependsOn("copyWebResources")
        if(project.gradle.startParameter.taskNames.contains("build")) exclude("application.yml")
    }
    bootJar {
        archiveFileName.set("avoid-project-worklist.jar")
    }
    jar {
        enabled = false
    }
    test {
        useJUnitPlatform()
    }
}
