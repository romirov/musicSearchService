
rootProject.name = "MusicSearchService"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings
    val openapiVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("io.kotest.multiplatform") version kotestVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
    }
}

//include("m1l1-quickstart")
include("acceptance")

include("api-v1-jackson")