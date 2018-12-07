package location.tracker.adapter

import io.reactivex.Completable
import location.tracker.domain.User

interface LocationCloudClient {

    fun createUser(user: User): Completable

}
