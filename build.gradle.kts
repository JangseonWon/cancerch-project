plugins {
    kotlin("jvm") version "1.7.10" apply false
    kotlin("kapt") version "1.7.10" apply false
}

subprojects {
    repositories {
        mavenCentral()
        maven(url = "http://gemini/api/packages/LIMS/maven"){
            isAllowInsecureProtocol = true
        }
        mavenLocal()
    }
    group = "com.gcgenome"
    version = "1.0"
}
