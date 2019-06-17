package nl.cloud.location.adapter;

import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.launch
import nl.cloud.location.user.Factory
import nl.cloud.location.user.UserService

class HttpVerticle : CoroutineVerticle() {

	val logger: Logger = LoggerFactory.getLogger(HttpVerticle::class.qualifiedName)

	lateinit var userService: UserService

	// Called when verticle is deployed
	override suspend fun start() {

		val router: Router = generateRouter()
		val port: Int = config.getInteger("http.port", 8081)

		// Start the server
	    val httpServer = vertx.createHttpServer()
	        .requestHandler(router)
			.listenAwait(port)

		val userService = Factory.createWithProxy(vertx);

		logger.info("Http server is running on port %s", port)
	}

	// Optional - called when verticle is undeployed
	override suspend fun stop() {
	}

	private fun generateRouter(): Router {
		val router = Router.router(vertx)
		router.get("/api/user/").coroutineHandler { context ->
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