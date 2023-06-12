import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
val logbackVersion: String by project
val kotlinVersion: String by project

fun ktor(module: String, prefix: String = "server-", version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    kotlin("plugin.serialization")
}

application {
    mainClass.set("com.crypto.ApplicationKt")
}
repositories {
    mavenCentral()
}


dependencies {
    implementation(ktor("core"))
    implementation(ktor("netty"))

    implementation(ktor("content-negotiation"))
    implementation(ktor("jackson", "serialization"))
    implementation(ktor("kotlinx-json", "serialization")) // TODO ???

    implementation(ktor("call-logging"))

    testImplementation(ktor("test-host"))
    testImplementation(ktor("content-negotiation", prefix = "client-"))
    testImplementation(kotlin("test-junit"))

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation(project(":common"))
    implementation(project(":api"))
    implementation(project(":mappers"))
    implementation(project(":business-logic"))
    implementation(project(":stubs"))
}
