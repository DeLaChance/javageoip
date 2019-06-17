package nl.cloud.location.domain.path

/**
 * Represents a location and a moment in time.
 */
data class TimedGeoLocation(
    val longitude: Double,
    val latitude: Double,
    val timestamp: Long
) {
    init {
        if( longitude < -180 || longitude > 180 ) {
            throw IllegalArgumentException("longitude out of range")
        } else if( latitude < -90 || latitude > 90 ) {
            throw IllegalArgumentException("latitude out of range")
        } else if( timestamp < 0 ) {
            throw IllegalArgumentException("timestamp less than 0")
        }
    }

    override fun toString(): String {
        return "TimedGeoLocation(longitude=$longitude, latitude=$latitude, timestamp=$timestamp)"
    }

}
