plugins {
    kotlin("jvm") version "1.5.20" apply false
    kotlin("kapt") version "1.5.20" apply false
}

subprojects {
    repositories {
        mavenCentral()
        mavenLocal()
    }
    group = "com.greencross"
    version = "1.0"
}
