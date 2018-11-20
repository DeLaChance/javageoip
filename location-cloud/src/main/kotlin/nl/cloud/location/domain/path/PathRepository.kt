package nl.cloud.location.domain.path

import nl.cloud.location.domain.user.UserId

interface PathRepository {
    fun fetchPathByUserId(userId: UserId): Path?
    fun createPath(userId: UserId): Path
    fun addLocationToPath(userId: UserId, location: TimedGeoLocation)
}