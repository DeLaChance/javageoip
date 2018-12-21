package location.tracker.adapter

import io.reactivex.Completable
import location.tracker.domain.TimedLocation
import location.tracker.domain.User

interface LocationCloudClient {

    fun createUser(user: User): Completable
    fun publishLocation(user: User, timedLocation: TimedLocation): Completable
}
