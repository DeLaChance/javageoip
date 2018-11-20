package nl.cloud.location.domain.path

import io.micronaut.context.annotation.Primary
import nl.cloud.location.domain.user.UserId

@Primary
class InMemoryPathRepository : PathRepository {

    private val paths: HashMap<UserId, Path> = hashMapOf()

    override fun createPath(userId: UserId): Path {
        val path: Path = Path(userId)
        this.paths.put(userId, path)
        return path
    }

    override fun fetchPathByUserId(userId: UserId): Path? {
        return this.paths.getOrDefault(userId, null)
    }

    override fun addLocationToPath(userId: UserId, location: TimedGeoLocation) {
        this.fetchPathByUserId(userId)?.addLocation(location)
    }

}