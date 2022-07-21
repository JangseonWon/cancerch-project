plugins {
    kotlin("jvm")
    id("org.wisepersist.gwt") version "1.1.18"
    id("java")
}

group = "com.gcgenome"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

dependencies {
    implementation(libs.elemento.core)
    implementation(libs.jackson.annotations)
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)
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
