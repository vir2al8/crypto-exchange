rootProject.name = "crypto-exchange"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        id("org.openapi.generator") version openapiVersion apply false
    }
}

include("api")
include("mappers")
include("common")