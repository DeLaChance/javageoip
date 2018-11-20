package nl.cloud.location.domain.path

import nl.cloud.location.domain.user.UserId

/**
 * A path is a list of timestamped locations ({@link TimedGeoLocation})
 * indicating the whereabouts of a user over time.
 */
class Path(val userId: UserId) {
    val locations: MutableList<TimedGeoLocation> = mutableListOf()

    fun addLocation(location: TimedGeoLocation) {
        locations.add(location)
    }
}