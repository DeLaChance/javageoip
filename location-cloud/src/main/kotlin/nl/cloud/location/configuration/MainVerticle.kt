package nl.cloud.location.configuration

import io.vertx.core.Verticle
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.core.deployVerticleAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.serviceproxy.ServiceBinder
import nl.cloud.location.adapter.HttpVerticle
import nl.cloud.location.user.UserRepositoryFactory
import nl.cloud.location.user.UserRepository
import kotlin.reflect.KClass

class MainVerticle : CoroutineVerticle() {

    val logger: Logger = LoggerFactory.getLogger(MainVerticle::class.qualifiedName)

    override suspend fun start() {
        logger.info("Starting up application 'location-cloud'")

        registerServices()
        deployVerticles()

        logger.info("Started up application")
    }

    fun registerServices() {
        registerService(UserRepository::class.java, UserRepositoryFactory.create(vertx), UserRepositoryFactory.EVENT_BUS_ADDRESS)
    }

    fun <T : Any> registerService(klass: Class<T>, implementation: T, eventBusAddress: String) {
        ServiceBinder(vertx)
            .setAddress(eventBusAddress)
            .register(klass, implementation)
        logger.info("Deployed service ${klass.simpleName} at eventbus address ${eventBusAddress}")

    }

    suspend fun deployVerticles() {
        deployVerticle(HttpVerticle::class)
    }

    suspend fun <T : Verticle> deployVerticle(kClass: KClass<T>) {
        val name: String = kClass.qualifiedName!!
        var id = vertx.deployVerticleAwait(name)
        logger.info("Deployed verticle ${kClass.qualifiedName} with id ${id}")
    }

}