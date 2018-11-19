package nl.cloud.location.adapter.http

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import nl.cloud.location.domain.User

@Controller("/api/users/")
class UserController {

    private val users: HashMap<String, User> = hashMapOf()

    init {
        this.addUser(User("John Snow"))
    }

    @Get("/hello/{name}")
    fun hello(name: String): String {
        return "Hello $name"
    }

    @Get("/")
    fun fetchUsers(): List<User> {
        return users.values.toList()
    }

    @Post("/")
    fun createUser(@Body user: User): User {
        this.addUser(user)

        return user
    }

    private fun addUser(user: User) {
        this.users.put(user.id, user)
    }
}
