package location.cloud

import io.micronaut.http.annotation.*

@Controller("/")
class HelloController {

    @Get("/hello/{name}")
    fun hello(name: String): String {
        return "Hello $name"
    }
}