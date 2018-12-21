package location.tracker.domain

import io.micronaut.context.annotation.Primary
import io.micronaut.scheduling.annotation.Scheduled
import location.tracker.adapter.LocationCloudClient
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrieves the location of a given user and publishes it on the network.
 */
@Singleton
class LocationPublisher {

    private val logger = LoggerFactory.getLogger(LocationPublisher::class.java)

    var repository: SingleUserRepository
    var locationRetriever: LocationRetriever
    var locationCloudClient: LocationCloudClient

    @Inject
    @Primary
    constructor(repository: SingleUserRepository,
        locationRetriever: LocationRetriever,
        locationCloudClient: LocationCloudClient) {

        this.repository = repository
        this.locationRetriever = locationRetriever
        this.locationCloudClient = locationCloudClient
    }

    @Scheduled(fixedDelay = "10s")
    fun publishLocation() {
        var user: User? = this.repository.fetchUser()

        if( user != null ) {
            var location: TimedLocation = this.locationRetriever.retrieveTimedLocation()
            this.locationCloudClient.publishLocation(user, location)

            logger.info("Publish location ${location} for user ${user}")
        } else {
            logger.info("Cannot publish user location, as there has not been created a user yet.")
        }
    }
}