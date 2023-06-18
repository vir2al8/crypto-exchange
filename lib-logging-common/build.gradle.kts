plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project
    val datetimeVersion: String by project

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test"))
}