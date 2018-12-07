package location.tracker.configuration

import io.micronaut.runtime.Micronaut
import org.slf4j.LoggerFactory

object Application {

    private val logger = LoggerFactory.getLogger(Application::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("location.tracker")
                .mainClass(Application.javaClass)
                .start()

        logger.info("Started up LocationTracker application")

    }
}