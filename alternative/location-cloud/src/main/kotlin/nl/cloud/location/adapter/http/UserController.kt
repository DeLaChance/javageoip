package nl.cloud.location.adapter.http

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import nl.cloud.location.domain.user.User
import nl.cloud.location.domain.user.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct
import javax.inject.Inject

@Controller("/api/users/")
class UserController {

    val logger: Logger = LoggerFactory.getLogger(UserController::class.qualifiedName)

    @Inject
    lateinit var repository: UserRepository

    @PostConstruct
    fun postConstruct() {
        logger.info("Started up the UserController")
    }

    @Get("/hello/{name}")
    fun hello(name: String): String {
        return "Hello $name"
    }

    @Get("/")
    fun fetchUsers(): List<User> {
        return this.repository.fetchUsers()
    }

    @Get("/{id}")
    fun fetchUserById(id: String): HttpResponse<User> {
        val user: User? = this.repository.findUserById(id)
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
        this.repository.addUser(user)

        return HttpResponse.created(user)
    }

    @Delete("/{id}")
    fun deleteUserById(id: String): HttpResponse<User> {
        val optionalUser: User? = this.repository.deleteUserById(id)
        val response: HttpResponse<User>

        if ( optionalUser != null ) {
            response = HttpResponse.ok()
        } else {
            response = HttpResponse.notFound()
        }

        return response
    }


}
