package nl.cloud.location.adapter.http

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import nl.cloud.location.domain.user.User
import java.util.*

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

    @Get("/{id}")
    fun fetchUserById(id: String): HttpResponse<User> {
        val user: User? = this.findUserById(id)
        val response: HttpResponse<User>

        if( user != null ) {
            response = HttpResponse.ok(user)
        } else {
            response = HttpResponse.notFound()
        }

        return response
    }

    @Post("/")
    fun createUser(@Body user: User): HttpResponse<User> {
        this.addUser(user)

        return HttpResponse.created(user)
    }

    @Delete("/{id}")
    fun deleteUserById(id: String): HttpResponse<User> {
        val optionalUser: User? = this.deleteUser(id)
        val response: HttpResponse<User>

        if ( optionalUser != null ) {
            response = HttpResponse.ok()
        } else {
            response = HttpResponse.notFound()
        }

        return response
    }

    private fun deleteUser(id: String): User? {
        return this.users.remove(id)
    }

    private fun addUser(user: User) {
        this.users.put(user.id, user)
    }

    private fun findUserById(id: String): User? {
        return this.users.getOrDefault(id, null)
    }
}
