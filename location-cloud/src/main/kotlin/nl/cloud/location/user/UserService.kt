package nl.cloud.location.user

import io.vertx.codegen.annotations.GenIgnore
import io.vertx.codegen.annotations.Nullable
import io.vertx.codegen.annotations.ProxyGen
import io.vertx.codegen.annotations.VertxGen
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.kotlin.core.Vertx

@ProxyGen
@VertxGen
interface UserService {

    fun findAll(handler: Handler<AsyncResult<List<User>>>)
    fun findByName(name: String, handler: Handler<AsyncResult<@Nullable User>>)
}

@GenIgnore
object Factory {

    @JvmStatic
    fun create(): UserService = UserServiceImpl()

    @JvmStatic
    fun createWithProxy(vertx: Vertx, address: String): UserService = UserServiceImpl()
}