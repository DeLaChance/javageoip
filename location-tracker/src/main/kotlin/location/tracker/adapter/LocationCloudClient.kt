package location.tracker.adapter

import io.reactivex.Completable
import io.reactivex.Single
import location.tracker.domain.TimedLocation
import location.tracker.domain.User
import java.util.*

interface LocationCloudClient {

    fun createUser(user: User): Completable
    fun findUserById(id: String): Single<Optional<User>>
    fun publishLocation(user: User, timedLocation: TimedLocation): Completable
}
