package location.cloud

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/")
class HelloController {

    init {
        println("Started HelloController")
    }

    @Get("/hello/{name}")
    fun hello(name: String): String {
        return "Hello $name"
    }
}