plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt").version("1.22.0")
}

val kotestVersion: String by project

dependencies {
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
            events = setOf(org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED, org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED)
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config = files("$projectDir/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
//    baseline = file("$projectDir/baseline.xml") // a way of suppressing issues before introducing detekt
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
        txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
        sarif.required.set(true) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with Github Code Scanning
        md.required.set(true) // simple Markdown format
    }
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "17"
}
tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = "17"
}