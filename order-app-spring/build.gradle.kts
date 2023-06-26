plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	kotlin("jvm")
	kotlin("plugin.spring")
	kotlin("plugin.serialization")
}

dependencies {
	val kotestVersion: String by project
	val springdocOpenapiUiVersion: String by project
	val coroutinesVersion: String by project

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocOpenapiUiVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${coroutinesVersion}")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:${coroutinesVersion}")
	implementation(project(":common"))
	implementation(project(":api"))
	implementation(project(":mappers"))
	implementation(project(":stubs"))
	implementation(project(":business-logic"))
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
	testImplementation("org.springframework.boot:spring-boot-starter-webflux")
	testImplementation("com.ninja-squad:springmockk:4.0.0")
}

tasks {
	withType<ProcessResources> {
		from("$rootDir/specs") {
			into("/static")
		}
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
