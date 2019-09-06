package nl.cloud.location.user

import io.vertx.codegen.annotations.GenIgnore
import io.vertx.codegen.annotations.Nullable
import io.vertx.codegen.annotations.ProxyGen
import io.vertx.codegen.annotations.VertxGen
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx

@ProxyGen
@VertxGen
interface UserRepository {

    fun addUser(user: User, handler: Handler<AsyncResult<@Nullable User>>)

    fun findAll(handler: Handler<AsyncResult<List<User>>>)
    fun findBy(userId: String, handler: Handler<AsyncResult<@Nullable User?>>)

}

@GenIgnore
object UserRepositoryFactory {

    val EVENT_BUS_ADDRESS = "nl.cloud.location.user.FileBasedUserRepository"

    @JvmStatic
    fun create(vertx: Vertx): UserRepository = FileBasedUserRepository(vertx)

    @JvmStatic
    fun createWithProxy(vertx: Vertx): UserRepository = UserRepositoryVertxEBProxy(vertx, EVENT_BUS_ADDRESS)
}