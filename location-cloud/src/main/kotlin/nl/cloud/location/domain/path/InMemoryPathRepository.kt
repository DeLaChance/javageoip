package nl.cloud.location.domain.path

import nl.cloud.location.domain.user.UserId
import javax.inject.Singleton

@Singleton
class InMemoryPathRepository : PathRepository {

    private val paths: HashMap<UserId, Path> = hashMapOf()

    override fun getPaths(): List<Path> {
        return this.paths.values.toList()
    }

    override fun createPath(userId: UserId): Path {
        val path: Path = Path(userId)
        this.paths.put(userId, path)
        return path
    }

    override fun fetchPathByUserId(userId: UserId): Path? {
        return this.paths.getOrDefault(userId, null)
    }

    override fun addLocationToPath(userTimedGeoLocation: UserTimedLocation) {
        this.addLocationToPath(UserId(userTimedGeoLocation.id), userTimedGeoLocation.timedLocation)
    }

    override fun addLocationToPath(userId: UserId, location: TimedGeoLocation) {
        this.fetchPathByUserId(userId)?.addLocation(location)
    }

}