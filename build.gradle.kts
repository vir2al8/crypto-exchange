import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt").version("1.22.0") // TODO ?
}

val JVM_TARGET = "17"

group = "com.crypto"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JVM_TARGET
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile> {
        kotlinOptions.jvmTarget = JVM_TARGET
    }
}

detekt {
    source = files("common", "api", "mappers", "order-app-spring")
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config = files("$projectDir/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/baseline.xml") // a way of suppressing issues before introducing detekt
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = JVM_TARGET
}
tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = JVM_TARGET
}