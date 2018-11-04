package nl.cloud.location.adapter.http

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/api/users/")
class UserController {

    init {
        println("Started UserController")
    }

    @Get("/hello/{name}")
    fun hello(name: String): String {
        return "Hello $name"
    }
}