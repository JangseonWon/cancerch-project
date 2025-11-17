plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    id("org.jetbrains.kotlin.plugin.spring") version "1.7.20"
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
kapt {
    keepJavacAnnotationProcessors = true
    javacOptions {
        option("--add-opens", "jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED")
        option("--add-opens", "jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED")
        option("--add-opens", "jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED")
        option("--add-opens", "jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED")
        option("--add-opens", "jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED")
        option("--add-opens", "jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED")
        option("--add-opens", "jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED")
        option("--add-opens", "jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED")
        option("--add-opens", "jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED")
        option("--add-opens", "jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED")
        option("--add-opens", "jdk.compiler/com.sun.tools.javac.jvm=ALL-UNNAMED")
    }
}

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
