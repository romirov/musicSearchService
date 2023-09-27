plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm { }

    sourceSets {
        val coroutinesVersion: String by project
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))

                // transport models
                implementation(project(":common"))
                implementation(project(":api-v1-kmp"))
                implementation(project(":mappers-v1"))

                // logging
                implementation(project(":api-log"))
                implementation(project(":mappers-log"))

                // Stubs
                implementation(project(":stubs"))

                // Biz
                implementation(project(":biz"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
//        val jvmMain by getting {
//        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

