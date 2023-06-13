package com.crypto.plugins

import com.crypto.configs.OrderAppConfigs
import com.crypto.controller.v1.createOrder
import com.crypto.controller.v1.deleteOrder
import com.crypto.controller.v1.readOrder
import com.crypto.controller.v1.searchOrder
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(appConfigs: OrderAppConfigs) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("api/v1") {
            v1Order(appConfigs)
        }

        static("static") {
            resources("static")
        }
    }
}

fun Route.v1Order(appConfigs: OrderAppConfigs) {
    route("order") {
        post("create") {
            call.createOrder(appConfigs)
        }
        post("read") {
            call.readOrder(appConfigs)
        }
        post("delete") {
            call.deleteOrder(appConfigs)
        }
        post("search") {
            call.searchOrder(appConfigs)
        }
    }
}
