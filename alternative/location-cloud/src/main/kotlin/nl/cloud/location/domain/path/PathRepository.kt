package nl.cloud.location.domain.path

import nl.cloud.location.domain.user.UserId

interface PathRepository {
    fun getPaths(): List<Path>
    fun fetchPathByUserId(userId: UserId): Path?
    fun createPath(userId: UserId): Path
    fun addLocationToPath(userId: UserId, location: TimedGeoLocation)
    fun addLocationToPath(userTimedGeoLocation: UserTimedLocation)
}