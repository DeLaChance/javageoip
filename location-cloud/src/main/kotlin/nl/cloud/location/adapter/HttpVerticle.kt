package nl.cloud.location.adapter;

import io.vertx.core.json.JsonArray
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.launch
import nl.cloud.location.user.UserRepositoryFactory
import nl.cloud.location.user.User
import nl.cloud.location.user.UserRepository

class HttpVerticle : CoroutineVerticle() {

	val logger: Logger = LoggerFactory.getLogger(HttpVerticle::class.qualifiedName)

	lateinit var userRepository: UserRepository

	override suspend fun start() {

		val router: Router = generateRouter()
		val port: Int = config.getInteger("http.port", 8081)

		// Start the server
	    vertx.createHttpServer()
	        .requestHandler(router)
			.listenAwait(port)

		userRepository = UserRepositoryFactory.createWithProxy(vertx)

		logger.info("Http server is running on port ${port}")
	}

	override suspend fun stop() {
	}

	private fun generateRouter(): Router {
		val router = Router.router(vertx)
		router.get("/api/user/").coroutineHandler { context ->
			val users: List<User> = awaitResult { handler ->
				userRepository.findAll(handler) }
			val jsonBlob: String = users.map(User::toJson)
				.fold(JsonArray()) { array, element -> array.add(element) }
				.encodePrettily()

			context.response().setStatusCode(200).end(jsonBlob)
		}

		router.get("/api/user/:userId").coroutineHandler { context ->
			val userId: String = context.request().getParam("userId")
			val user: User? = awaitResult { handler -> userRepository.findBy(userId, handler) }
			if (user == null) {
				context.response().setStatusCode(404).end("User with userid ${userId} not found")
			} else {
				context.response().setStatusCode(200).end(user.toJson().encodePrettily())
			}
		}

		return router
	}

	/**
	 * An extension method for simplifying coroutines usage with Vert.x Web routers
	 */
	fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit) {
		handler { ctx ->
			launch(ctx.vertx().dispatcher()) {
				try {
					fn(ctx)
				} catch (e: Exception) {
					logger.error("Exception in coroutinehandler: ", e)
					ctx.fail(e)
				}
			}
		}
	}
}