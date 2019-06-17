package nl.cloud.location.adapter.http

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import nl.cloud.location.domain.path.Path
import nl.cloud.location.domain.path.PathRepository
import nl.cloud.location.domain.path.TimedGeoLocation
import nl.cloud.location.domain.path.UserTimedLocation
import nl.cloud.location.domain.user.User
import nl.cloud.location.domain.user.UserId
import nl.cloud.location.domain.user.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.annotation.Nullable
import javax.inject.Inject

/**
 * A controller for CRUD on {@link Path} entities.
 */
@Controller("/api/paths/")
class PathController {

    val logger: Logger = LoggerFactory.getLogger(PathController::class.qualifiedName)

    @Inject
    lateinit var pathRepository: PathRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Get("/")
    fun getPaths(): HttpResponse<List<Path>> {
        return HttpResponse.ok(this.pathRepository.getPaths())
    }

    @Get("/{userId}/locations{?startTime,endTime}")
    fun fetchPathByUserId(userId: String, @Nullable startTime: Long?, @Nullable endTime: Long?): HttpResponse<List<TimedGeoLocation>> {
        val path: Path? = this.pathRepository.fetchPathByUserId(UserId(userId))
        val httpResponse: HttpResponse<List<TimedGeoLocation>>

        if (path == null) {
            httpResponse = HttpResponse.notFound()
        } else {
            var points: List<TimedGeoLocation> = path.toTimeFilteredPointList(startTime, endTime)

            httpResponse = HttpResponse.ok(points)
        }

        return httpResponse
    }

    @Post("/")
    fun addLocationToPath(@Body userTimedGeoLocation: UserTimedLocation): HttpResponse<Path> {
        val userId = UserId(userTimedGeoLocation.id)
        val user: User? = this.userRepository.findUserById(userTimedGeoLocation.id)

        val httpResponse: HttpResponse<Path>
        if (user == null) {
            httpResponse = HttpResponse.notFound()
        } else {
            // TODO: put this kind of code in a service class
            var path: Path? = this.pathRepository.fetchPathByUserId(userId)

            if (path == null) {
                path = this.pathRepository.createPath(userId)
            }

            this.pathRepository.addLocationToPath(userTimedGeoLocation)

            httpResponse = HttpResponse.ok(path)
        }

        return httpResponse
    }
}