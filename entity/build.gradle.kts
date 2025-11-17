plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    id("org.jetbrains.kotlin.plugin.spring") version "1.7.20"
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
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}