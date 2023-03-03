plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(project(":api"))
    implementation(project(":common"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation(kotlin("stdlib-jdk8"))

//        implementation(kotlin("stdlib"))
//        testImplementation(kotlin("test-junit"))

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}