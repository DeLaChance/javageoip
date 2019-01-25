package location.tracker.domain

import io.micronaut.context.annotation.Primary
import io.micronaut.retry.annotation.Retryable
import io.micronaut.scheduling.annotation.Scheduled
import io.reactivex.Completable
import location.tracker.adapter.LocationCloudClient
import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct
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

    var userExistsInCloud = false

    @Inject
    @Primary
    constructor(repository: SingleUserRepository,
        locationRetriever: LocationRetriever,
        locationCloudClient: LocationCloudClient) {

        this.repository = repository
        this.locationRetriever = locationRetriever
        this.locationCloudClient = locationCloudClient
    }

    @PostConstruct
    @Retryable(attempts = "5", delay = "2s")
    fun tryAddUserToCloud() {
        var newUser: User? = this.repository.fetchUser()

        if ( newUser != null ) {
            this.locationCloudClient.findUserById(newUser.id)
                .flatMapCompletable({optional ->
                    if( optional.isPresent ) {
                        Completable.complete()
                    } else {
                        this.locationCloudClient.createUser(newUser)
                    }
                })
                .subscribe(
                    {
                        this.userExistsInCloud = true
                        logger.info("User has been created or did already exist.")
                    },
                    { throwable -> logger.error("Could not create the user or something else went wrong: ", throwable) }
                )
        }
    }

    @Scheduled(fixedDelay = "5s", initialDelay = "5s")
    fun publishLocation() {
        var user: User? = this.repository.fetchUser()

        if( user == null ) {
            logger.info("Cannot publish user location, as there has not been defined a user for this tracker.")
        } else if( this.userExistsInCloud == false ) {
            logger.info("Cannot publish user location, as the user does not exist in the location-cloud yet.")
        } else {
            var location: TimedLocation = this.locationRetriever.retrieveTimedLocation()
            val userTimedLocation: UserTimedLocation = UserTimedLocation(user, location)
            this.locationCloudClient.publishLocation(userTimedLocation)
                    .subscribe(
                            {
                                logger.info("Successfully published location ${location} for user ${user}.")
                            },
                            { error -> logger.error("Error while publishing location: ", error) }
                    )
        }
    }
}