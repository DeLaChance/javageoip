package nl.cloud.location.user

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler

class UserServiceImpl : UserService {

    val users: List<User> = listOf(User("John Snow"), User("Cersei Lannister"))

    override fun findAll(handler: Handler<AsyncResult<List<User>>>) {
        handler.handle(Future.succeededFuture(users))
    }

    override fun findBy(userId: String, handler: Handler<AsyncResult<User?>>) {
        val matchingElement: User? = users.filter { user -> user.id.toString().equals(userId) }
            .firstOrNull()
        handler.handle(Future.succeededFuture(matchingElement))
    }

}