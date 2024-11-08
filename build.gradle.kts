plugins {
    kotlin("jvm") version "1.7.10" apply false
    kotlin("kapt") version "1.7.10" apply false
}

subprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven(url = "https://maven.pkg.github.com/GC-Genome/maven") {
            credentials {
                username = (project.findProperty("github_username") ?: System.getenv("GITHUB_USERNAME")).toString()
                password = (project.findProperty("github_password") ?: System.getenv("GITHUB_TOKEN")).toString()
            }
        }
        maven(url = "http://gitea.apps.gcgenome.com/api/packages/LIMS/maven"){
            isAllowInsecureProtocol = true
        }
        maven(url = "http://gemini/api/packages/LIMS/maven"){
            isAllowInsecureProtocol = true
        }
    }
    group = "com.gcgenome"
    version = "1.0"
}
