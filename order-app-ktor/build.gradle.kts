import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
val logbackVersion: String by project
val kotlinVersion: String by project

fun ktor(module: String, prefix: String = "server-", version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"
fun ktorServer(module: String, version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-server-$module:$version"
fun ktorClient(module: String, version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-client-$module:$version"

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
    implementation(ktorServer("core"))
    implementation(ktorServer("netty"))

    implementation(ktorServer("content-negotiation"))
    implementation(ktor("jackson", "serialization"))
    implementation(ktor("kotlinx-json", "serialization")) // TODO ???

    implementation(ktorServer("call-logging"))

    testImplementation(ktorServer("test-host"))
    testImplementation(ktorClient("content-negotiation"))
    testImplementation(kotlin("test-junit"))

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation(ktorServer("auth-jwt"))

    implementation(project(":common"))
    implementation(project(":api"))
    implementation(project(":mappers"))
    implementation(project(":business-logic"))
    implementation(project(":repository-cassandra"))
    implementation(project(":stubs"))
    testImplementation(project(":repository-stubs"))
    testImplementation(project(":repository-tests"))
}
