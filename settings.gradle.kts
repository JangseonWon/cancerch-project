rootProject.name = "avoid-project"
include("service")
include("gateway")
include("entity")
include("shared")
include("analysis")
include("analysis-ui")
include("analysis-crawler")
include("report")
include("report-ui")
include("worklist-ui")
include("worklist-api")
include("search")
include("worklist-info-ui")
include("alis")
include("publish")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("reflect", "org.jetbrains.kotlin", "kotlin-reflect").withoutVersion()
            library("stdlib-jdk8", "org.jetbrains.kotlin", "kotlin-stdlib-jdk8").withoutVersion()
            bundle("kotlin", listOf("reflect", "stdlib-jdk8"))

            library("kotlin-reactor", "io.projectreactor.kotlin", "reactor-kotlin-extensions").withoutVersion()
            library("kotlin-coroutines-reactor", "org.jetbrains.kotlinx", "kotlinx-coroutines-reactor").withoutVersion()
            library("kotlin-jackson", "com.fasterxml.jackson.module", "jackson-module-kotlin").withoutVersion()
            library("webflux", "org.springframework.boot", "spring-boot-starter-webflux").withoutVersion()
            bundle("kotlin-webflux", listOf("reflect", "stdlib-jdk8", "kotlin-reactor", "kotlin-coroutines-reactor", "kotlin-jackson", "webflux"))

            library("spring-gateway", "org.springframework.cloud", "spring-cloud-starter-gateway").withoutVersion()
            library("spring-discovery", "org.springframework.cloud", "spring-cloud-starter-zookeeper-discovery").withoutVersion()
            library("spring-log4j2", "org.springframework.boot", "spring-boot-starter-log4j2").withoutVersion()
            library("spring-security", "org.springframework.boot", "spring-boot-starter-security").withoutVersion()
            bundle("spring-client", listOf("spring-discovery", "spring-log4j2", "spring-security"))

            library("r2dbc", "org.springframework.boot", "spring-boot-starter-data-r2dbc").withoutVersion()
            library("r2dbc-postgres", "io.r2dbc", "r2dbc-postgresql").version { require("0.8.12.RELEASE") }
            bundle("r2dbc-postgres", listOf("r2dbc", "r2dbc-postgres"))

            library("querydsl-core", "com.querydsl", "querydsl-core").withoutVersion()
            library("querydsl-apt", "com.querydsl", "querydsl-apt").withoutVersion()
            library("querydsl-r2dbc", "com.infobip", "infobip-spring-data-r2dbc-querydsl-boot-starter").version { require("7.0.0") }
            bundle("r2dbc-querydsl", listOf("querydsl-core", "querydsl-apt", "querydsl-r2dbc"))

            library("spring-boot-test", "org.springframework.boot", "spring-boot-starter-test").withoutVersion()
            library("mockito-kotlin", "org.mockito.kotlin", "mockito-kotlin").version { require("4.0.0") }
            library("mockito-inline", "org.mockito", "mockito-inline").withoutVersion()
            library("reactor-test", "io.projectreactor", "reactor-test").withoutVersion()
            library("kotlin-test", "org.jetbrains.kotlin", "kotlin-test").withoutVersion()
            bundle("test", listOf("spring-boot-test", "mockito-kotlin", "mockito-inline", "reactor-test", "kotlin-test"))

            library("spring-cloud-bom", "org.springframework.cloud", "spring-cloud-dependencies").version { require("2021.0.3") }

            library("elemento-core", "org.jboss.elemento", "elemento-core").version { require("1.0.10") }
            library("elemental2-svg", "com.google.elemental2", "elemental2-svg").version { require("1.1.0") }
            library("gwt-user", "org.gwtproject", "gwt-user").version { require("2.10.0") }
            library("gwt-dev", "org.gwtproject", "gwt-dev").version { require("2.10.0") }
            bundle("gwt", listOf("elemento-core", "elemental2-svg", "gwt-user"))
            library("lombok", "org.projectlombok", "lombok").version { require("1.18.24") }
            library("jackson-annotations", "com.fasterxml.jackson.core", "jackson-annotations").version { require("2.13.3") }
        }
    }
}
