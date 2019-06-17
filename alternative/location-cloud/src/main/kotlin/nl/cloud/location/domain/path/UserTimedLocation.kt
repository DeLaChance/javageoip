package nl.cloud.location.domain.path

import nl.cloud.location.domain.user.User

/**
 * Represents a person being at a certain location and at a certain time
 */
class UserTimedLocation(
    val id: String,
    val timedLocation: TimedGeoLocation
) {
    constructor(user: User, timedLocation: TimedGeoLocation) : this(user.id, timedLocation)

}