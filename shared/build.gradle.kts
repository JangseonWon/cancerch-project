plugins {
    kotlin("jvm")
    id("org.wisepersist.gwt") version "1.1.15"
    id("java")
}

group = "com.greencross"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.jboss.elemento:elemento-core:1.0.3")
    implementation("com.google.gwt:gwt-user:2.9.0")
    implementation("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    compileOnly("org.springframework.boot:spring-boot-starter:2.6.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
}
val lombok = project.configurations.annotationProcessor.get().filter { it.name.startsWith("lombok") }.single()
tasks {
    gwt {
        minHeapSize = "1024M"
        maxHeapSize = "2048M"
        jsInteropExports.setGenerate(true)
        compiler.apply {
            localWorkers = 12
            disableClassMetadata = true
            disableCastChecking = true
        }
    }
    compileGwt {
        extraJvmArgs = listOf("-XX:ReservedCodeCacheSize=512M","-javaagent:${lombok}=ECJ", "-generateJsInteropExport")
    }
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
tasks.withType<Jar> {
    from(sourceSets.main.get().allSource)
    archiveFileName.set("avoid-shared.jar")
}
tasks.withType<Delete> {
    doFirst {
        delete("build/")
    }
}
