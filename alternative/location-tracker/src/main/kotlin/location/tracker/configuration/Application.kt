package location.tracker.configuration

import io.micronaut.runtime.Micronaut
import location.tracker.domain.LocationPublisher
import org.slf4j.LoggerFactory

object Application {

    private val logger = LoggerFactory.getLogger(Application::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        var app = Micronaut.build()
                .packages("location.tracker")
                .mainClass(javaClass)
                .start()

        // TODO: need a way to do this outside of the code, e.g. via annotation or configuration.
        app.createBean(LocationPublisher::class.java)


        logger.info("Started up LocationTracker application")
    }
}