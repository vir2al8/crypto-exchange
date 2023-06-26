plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":stubs"))

    testImplementation(project(":repository-tests"))

    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test"))
}