package location.tracker.adapter

import io.reactivex.Completable
import location.tracker.domain.UserDto

interface LocationCloudClient {

    fun createUser(user: UserDto): Completable

}
