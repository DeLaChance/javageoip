package location.tracker.adapter

import io.reactivex.Completable
import io.reactivex.Single
import location.tracker.domain.User
import location.tracker.domain.UserTimedLocation
import java.util.*

interface LocationCloudClient {

    fun createUser(user: User): Completable
    fun findUserById(id: String): Single<Optional<User>>
    fun publishLocation(userTimedLocation: UserTimedLocation): Completable
}
