plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project
    val datetimeVersion: String by project
    val logbackVersion: String by project
    val logbackEncoderVersion: String by project
    val janinoVersion: String by project

    implementation(project(":lib-logging-common"))

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")
    implementation("org.codehaus.janino:janino:$janinoVersion")
    api("ch.qos.logback:logback-classic:$logbackVersion")

    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test"))
}