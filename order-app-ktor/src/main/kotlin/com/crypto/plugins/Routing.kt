package com.crypto.plugins

import com.crypto.controller.v1.createOrder
import com.crypto.controller.v1.deleteOrder
import com.crypto.controller.v1.readOrder
import com.crypto.controller.v1.searchOrder
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("api/v1") {
            v1Order()
        }

        static("static") {
            resources("static")
        }
    }
}

fun Route.v1Order() {
    route("order") {
        post("create") {
            call.createOrder()
        }
        post("read") {
            call.readOrder()
        }
        post("delete") {
            call.deleteOrder()
        }
        post("search") {
            call.searchOrder()
        }
    }
}
