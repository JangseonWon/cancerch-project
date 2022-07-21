plugins {
    kotlin("jvm") version "1.7.10" apply false
    kotlin("kapt") version "1.7.10" apply false
}

subprojects {
    repositories {
        mavenCentral()
        mavenLocal()
    }
    group = "com.gcgenome"
    version = "1.0"
}
