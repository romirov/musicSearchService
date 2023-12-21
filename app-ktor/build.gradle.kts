import org.codehaus.groovy.tools.shell.util.Logger.io
import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
val logbackVersion: String by project
val serializationVersion: String by project
val testcontainersVersion: String by project
val kmpUUIDVersion: String by project

// ex: Converts to "io.ktor:ktor-ktor-server-netty:2.0.1" with only ktor("netty")
fun ktor(module: String, prefix: String = "server-", version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    id("application")
    kotlin("plugin.serialization")
    kotlin("multiplatform")
    id("io.ktor.plugin")
}

val webjars: Configuration by configurations.creating
dependencies {
    val swaggerUiVersion: String by project
    webjars("org.webjars:swagger-ui:$swaggerUiVersion")
}
//repositories {
//    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
//}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

ktor {
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(JavaVersion.VERSION_17)
    }
}

jib {
    container.mainClass = "io.ktor.server.cio.EngineMain"
}

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(ktor("core")) // "io.ktor:ktor-server-core:$ktorVersion"
                implementation(ktor("cio"))

                // jackson
                implementation(ktor("jackson", "serialization")) // io.ktor:ktor-serialization-jackson
                implementation(ktor("content-negotiation")) // io.ktor:ktor-server-content-negotiation
                implementation(ktor("kotlinx-json", "serialization")) // io.ktor:ktor-serialization-kotlinx-json

                implementation(ktor("locations"))
                implementation(ktor("caching-headers"))
                implementation(ktor("call-logging"))
                implementation(ktor("auto-head-response"))
                implementation(ktor("cors")) // "io.ktor:ktor-cors:$ktorVersion"
                implementation(ktor("default-headers")) // "io.ktor:ktor-cors:$ktorVersion"
                implementation(ktor("cors")) // "io.ktor:ktor-cors:$ktorVersion"
                implementation(ktor("auto-head-response"))

                implementation(ktor("websockets")) // "io.ktor:ktor-websockets:$ktorVersion"
                implementation(ktor("auth")) // "io.ktor:ktor-auth:$ktorVersion"
                implementation(ktor("auth-jwt")) // "io.ktor:ktor-auth-jwt:$ktorVersion"

                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("com.sndyuk:logback-more-appenders:1.8.8")
                implementation("org.fluentd:fluent-logger:0.3.4")

                // transport models
                implementation(project(":lib-logging-logback"))
                implementation(project(":common"))
                implementation(project(":app-common"))
                implementation(project(":biz"))
                implementation(project(":api-v1-kmp"))
                implementation(project(":mappers-v1"))
                implementation(project(":stubs"))
                implementation(project(":api-log"))
                implementation(project(":mappers-log"))
                implementation(project(":lib-logging-common"))
                implementation(project(":lib-logging-logback"))

                implementation(project(":repo-postgresql"))
                implementation(project(":repo-in-memory"))
                implementation(project(":repo-stubs"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(ktor("test-host")) // "io.ktor:ktor-server-test-host:$ktorVersion"
                implementation(ktor("content-negotiation", prefix = "client-"))
                implementation(ktor("websockets", prefix = "client-"))

                implementation("org.testcontainers:postgresql:$testcontainersVersion")
                implementation("com.benasher44:uuid:$kmpUUIDVersion")
            }
        }
    }
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
