plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val kotestVersion: String by project

    testImplementation(kotlin("test-junit5"))

//    implementation("io.kotest:kotest-framework-engine:$kotestVersion")
//    implementation("io.kotest:kotest-framework-datatest:$kotestVersion")
//    implementation("io.kotest:kotest-assertions-core:$kotestVersion")
//    implementation("io.kotest:kotest-property:$kotestVersion")
    implementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.crypto.cryptoexchange.common.MainKt"
    }
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
//        filter {
//            isFailOnNoMatchingTests = false
//        }
        testLogging {
            showExceptions = true
            showStandardStreams = true
            events = setOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
            )
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}