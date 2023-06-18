plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project

    implementation(project(":common"))
    implementation(project(":stubs"))
    implementation(project(":lib-cor"))

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    testImplementation(project(":repository-stubs"))
    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test"))
}