package location.tracker.adapter.http

import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.reactivex.Completable
import location.tracker.adapter.LocationCloudClient
import location.tracker.domain.User
import javax.inject.Inject

/**
 * Communicates with a running instance of the LocationCloud app.
 */
class LocationCloudHttpClient : LocationCloudClient {

    @Value("micronaut.location-cloud.apis.users")
    lateinit var usersApiLocation: String

    @Value("micronaut.location-cloud.apis.paths")
    lateinit var pathsApiLocation: String

    @Client("\${micronaut.location-cloud.apis.base}")
    @Inject
    lateinit var httpClient: RxHttpClient

    override fun createUser(user: User): Completable {
        return httpClient.exchange(HttpRequest.POST(usersApiLocation, user))
            .firstOrError()
            .flatMapCompletable { response ->
                if( response.status == HttpStatus.OK ) {
                    Completable.complete()
                } else {
                    Completable.error(Exception("Could not create user with name: " + user.name))
                }
            }
    }

}