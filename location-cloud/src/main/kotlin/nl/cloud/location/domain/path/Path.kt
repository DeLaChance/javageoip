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

    fun toTimeFilteredPointList(startTime: Long?, endTime: Long?): List<TimedGeoLocation> {
        val selectedStartTime: Long
        val selectedEndTime : Long

        if (startTime == null) {
            selectedStartTime = Long.MIN_VALUE;
        } else {
            selectedStartTime = startTime
        }

        if (endTime == null) {
            selectedEndTime = Long.MAX_VALUE
        } else {
            selectedEndTime = endTime
        }

        return locations.filter { location ->
            location.timestamp in selectedStartTime..selectedEndTime
        }
        .toList()
    }
}