package nl.cloud.location.adapter.http

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import nl.cloud.location.domain.User

@Controller("/api/users/")
class UserController {

    private val users: List<User> = listOf(User("John Snow"))

    @Get("/hello/{name}")
    fun hello(name: String): String {
        return "Hello $name"
    }

    @Get("/")
    fun fetchUsers(): List<User> {
        return users
    }
}