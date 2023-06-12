rootProject.name = "crypto-exchange"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings
    val openapiVersion: String by settings
    val springframeworkBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val pluginSpringVersion: String by settings
    val ktorVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        id("io.kotest.multiplatform") version kotestVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false

        id("org.springframework.boot") version springframeworkBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        kotlin("plugin.spring") version pluginSpringVersion apply false

        id("io.ktor.plugin") version ktorVersion apply false
    }
}

include("api")
include("mappers")
include("common")
include("stubs")
include("lib-cor")
include("business-logic")
include("order-app-spring")
include("order-app-ktor")
include("order-app-rabbit")