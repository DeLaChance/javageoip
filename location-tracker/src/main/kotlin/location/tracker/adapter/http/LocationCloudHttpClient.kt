package location.tracker.adapter.http

import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Value
import io.reactivex.Completable
import io.reactivex.Single
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.client.WebClient
import location.tracker.adapter.LocationCloudClient
import location.tracker.domain.User
import location.tracker.domain.UserTimedLocation
import org.slf4j.LoggerFactory
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

    // TODO: would prefer to use Micronaut http client, but too unreliable.
    var vertx: Vertx = Vertx.vertx()
    var httpClient: WebClient = WebClient.create(vertx)

    @PostConstruct
    fun postConstruct() {
        logger.info("Using base url: ${baseUrl}")
        logger.info("Using users url: ${baseUrl}${usersApiLocation}")
        logger.info("Using paths url: ${baseUrl}${pathsApiLocation}")
    }

    override fun createUser(user: User): Completable {
        val url = "${baseUrl}${usersApiLocation}"

        return httpClient.requestAbs(HttpMethod.POST, url)
            .rxSendJson(JsonObject(user.toString()))
            .flatMapCompletable { httpResponse ->
                if( httpResponse.statusCode() == 200 ) {
                    Completable.complete()
                } else {
                    Completable.error(Exception("Could not create user ${user}."))
                }
            }
    }

    override fun findUserById(id: String): Single<Optional<User>> {
        val url = "${baseUrl}${usersApiLocation}${id}"

        return httpClient.requestAbs(HttpMethod.GET, url)
                .rxSend()
                .flatMap { httpResponse ->
                    var single: Single<Optional<User>>

                    if (httpResponse.statusCode() == 200) {
                        val jsonObject = httpResponse.bodyAsJsonObject()
                        val user: User = User(jsonObject.getString("id"),
                            jsonObject.getString("name"),
                            jsonObject.getJsonArray("keyWords", JsonArray())
                                .toMutableList()
                                .map { obj -> obj.toString() }
                                .toList()
                        )
                        single = Single.just(Optional.of(user))
                    } else if (httpResponse.statusCode() == 404) {
                        single = Single.just(Optional.empty())
                    } else {
                        single = Single.error(Exception("Could not find user with id ${id}.\n" +
                                "Status is: ${httpResponse.statusCode()} with reason ${httpResponse.statusMessage()}.")
                        )
                    }

                    single
                }
    }

    override fun publishLocation(userTimedLocation: UserTimedLocation): Completable {
        val url = "${baseUrl}${pathsApiLocation}"

        return httpClient.requestAbs(HttpMethod.POST, url)
            .rxSendJson(JsonObject(userTimedLocation.toString()))
            .flatMapCompletable { httpResponse ->
                if (httpResponse.statusCode() == 200) {
                    Completable.complete()
                } else {
                    Completable.error(Exception("Could not add location ${userTimedLocation}.\n" +
                        "Status is: ${httpResponse.statusCode()} with reason ${httpResponse.statusMessage()}."))
                }
            }
    }

}