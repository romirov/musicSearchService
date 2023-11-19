
rootProject.name = "MusicSearchService"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings
    val openapiVersion: String by settings
    val ktorVersion: String by settings
    val pluginShadow: String by settings
    val bmuschkoVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("io.ktor.plugin") version ktorVersion apply false

        id("io.kotest.multiplatform") version kotestVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
        id("com.github.johnrengelman.shadow") version pluginShadow apply false

        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false
    }
}

//include("m1l1-quickstart")
include("acceptance")

include("api-v1-kmp")
include("api-log")

include("common")
include("mappers-v1")
include("mappers-log")

include("stubs")

include("biz")
include("lib-cor")

include("app-common")
include("app-ktor")
include("app-kafka")

include("lib-logging-common")
include("lib-logging-logback")

include("repo-stubs")
include("repo-tests")
include("repo-in-memory")
include("repo-postgresql")

include("auth")