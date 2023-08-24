import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") apply false
}

group = "ru.mss"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {
    group = rootProject.group
    version = rootProject.version
    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}
