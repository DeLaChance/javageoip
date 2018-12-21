package location.tracker.domain

/**
 * Represents a person being at a certain location and at a certain time
 */
class UserTimedLocation(
    val id: String,
    val timedLocation: TimedLocation
) {
    constructor(user: User, timedLocation: TimedLocation) : this(user.id, timedLocation)

}