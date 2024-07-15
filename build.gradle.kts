plugins {
    kotlin("jvm") version "1.7.10" apply false
    kotlin("kapt") version "1.7.10" apply false
}

subprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven(url = "http://gemini/api/packages/LIMS/maven"){
            isAllowInsecureProtocol = true
        }
        maven(url = "http://gitea.apps.gcgenome.com/api/packages/LIMS/maven"){
            isAllowInsecureProtocol = true
        }
    }
    group = "com.gcgenome"
    version = "1.0"
}
