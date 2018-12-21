package location.tracker.adapter.http

import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.reactivex.Completable
import io.reactivex.Single
import location.tracker.adapter.LocationCloudClient
import location.tracker.domain.TimedLocation
import location.tracker.domain.User
import location.tracker.domain.UserTimedLocation
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*
import javax.annotation.PostConstruct

/**
 * Communicates with a running instance of the LocationCloud app.
 */
@Primary
class LocationCloudHttpClient : LocationCloudClient {

    private val logger = LoggerFactory.getLogger(LocationCloudHttpClient::class.java)

    @Value("\${micronaut.location-cloud.apis.base}")
    lateinit var baseUrl: String

    @Value("\${micronaut.location-cloud.apis.users}")
    lateinit var usersApiLocation: String

    @Value("\${micronaut.location-cloud.apis.paths}")
    lateinit var pathsApiLocation: String

    // TODO: would prefer the @Client and @Inject way, but that is not working for me.
    lateinit var httpClient: RxHttpClient

    @PostConstruct
    fun postConstruct() {
        httpClient = RxHttpClient.create(URL(baseUrl))
    }

    override fun createUser(user: User): Completable {
        return httpClient.exchange(HttpRequest.POST(usersApiLocation, user))
            .firstOrError()
            .flatMapCompletable { response ->
                if( response.status == HttpStatus.OK ) {
                    Completable.complete()
                } else {
                    Completable.error(Exception("Could not create user ${user}."))
                }
            }
    }

    override fun findUserById(id: String): Single<Optional<User>> {
        return httpClient.exchange(usersApiLocation + id, User::class.java)
                .firstElement()
                .flatMapSingle { response ->
                    if( response.status == HttpStatus.OK && response.body() != null ) {
                        Single.just(response.body)
                    } else if( response.status == HttpStatus.NOT_FOUND ) {
                        Single.just(Optional.empty())
                    } else {
                        Single.error(Exception("Could not find user with id ${id}.\n" +
                            "Status is: ${response.status} with reason ${response.reason()}.")
                        )
                    }
                }
    }

    override fun publishLocation(user: User, timedLocation: TimedLocation): Completable {
        val userTimedLocation = UserTimedLocation(user, timedLocation)

        return httpClient.exchange(HttpRequest.POST(pathsApiLocation, userTimedLocation))
            .firstOrError()
            .flatMapCompletable { response ->
                if( response.status == HttpStatus.OK ) {
                    Completable.complete()
                } else {
                    Completable.error(Exception("Could not add location ${timedLocation} to user ${user}.\n" +
                        "Status is: ${response.status} with reason ${response.reason()}."))
                }
            }
    }

}