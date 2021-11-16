plugins {
    kotlin("jvm") version "1.6.0-RC2" apply false
    kotlin("kapt") version "1.6.0-RC2" apply false
}

subprojects {
    repositories {
        mavenCentral()
        mavenLocal()
    }
    group = "com.greencross"
    version = "1.0"
}
