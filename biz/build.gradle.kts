plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}

    sourceSets {
        val coroutinesVersion: String by project

        //all { languageSettings.optIn("kotlin.RequiresOptIn") }

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(project(":common"))
                implementation(project(":auth"))
                implementation(project(":repo-in-memory"))
                implementation(project(":stubs"))
                implementation(project(":lib-cor"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(project(":repo-tests"))
                implementation(project(":repo-stubs"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
