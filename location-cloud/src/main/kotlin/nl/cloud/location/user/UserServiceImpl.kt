package nl.cloud.location.user

import io.vertx.core.AsyncResult
import io.vertx.core.Handler

class UserServiceImpl : UserService {
    override fun findAll(handler: Handler<AsyncResult<List<User>>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByName(name: String, handler: Handler<AsyncResult<User>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}