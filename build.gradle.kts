plugins {
    kotlin("jvm") version "1.9.23" apply false
    kotlin("kapt") version "1.9.23" apply false
}

import org.gradle.api.plugins.JavaPluginExtension

subprojects {
    apply(plugin = "java")
    repositories {
        mavenCentral()
        mavenLocal()
        maven(url = "https://maven.pkg.github.com/GC-Genome/maven") {
            credentials {
                username = (project.findProperty("github_username") ?: System.getenv("GITHUB_USERNAME")).toString()
                password = (project.findProperty("github_password") ?: System.getenv("GITHUB_TOKEN")).toString()
            }
        }
        maven(url = "http://172.19.216.21/api/packages/LIMS/maven"){
            isAllowInsecureProtocol = true
        }
        maven(url = "http://gemini/api/packages/LIMS/maven"){
            isAllowInsecureProtocol = true
        }
    }
    group = "com.gcgenome"
    version = "1.0"

    configure<org.gradle.api.plugins.JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.addAll(listOf(
            "--add-opens=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac.jvm=ALL-UNNAMED"
        ))
    }
}
