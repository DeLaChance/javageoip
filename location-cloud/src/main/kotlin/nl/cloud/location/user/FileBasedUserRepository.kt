package nl.cloud.location.user

import io.vertx.codegen.annotations.Nullable
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.kotlin.core.file.existsAwait
import io.vertx.kotlin.core.file.mkdirAwait
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FileBasedUserRepository : UserRepository {

    val vertx: Vertx
    val directory: String

    constructor(vertx: Vertx) {
        this.vertx = vertx

        directory = vertx.orCreateContext.config().getString("repository.directory", "./")

        GlobalScope.launch(vertx.dispatcher()) {
            createDirectoryIfNotExists()
        }
    }

    override fun addUser(user: User, handler: Handler<AsyncResult<@Nullable User>>) {
        handler.handle(Future.succeededFuture())
    }

    override fun findAll(handler: Handler<AsyncResult<List<User>>>) {
        handler.handle(Future.succeededFuture())
    }

    override fun findBy(userId: String, handler: Handler<AsyncResult<User?>>) {
        handler.handle(Future.succeededFuture())
    }

    suspend fun createDirectoryIfNotExists() {
        val directoryExists: Boolean = vertx.fileSystem().existsAwait(directory)
        if (!directoryExists) {
            vertx.fileSystem().mkdirAwait(directory)
        }
    }

}