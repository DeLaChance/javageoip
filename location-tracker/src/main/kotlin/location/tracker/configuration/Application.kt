package location.tracker.configuration

import io.micronaut.runtime.Micronaut
import location.tracker.domain.FileBasedSingleUserRepository
import org.slf4j.LoggerFactory

object Application {

    private val logger = LoggerFactory.getLogger(Application::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("location.tracker")
                .mainClass(Application.javaClass)
                .start()
                // TODO: need a way to do this outside of the code, e.g. via annotation or configuration.
                .createBean(FileBasedSingleUserRepository::class.java)

        logger.info("Started up LocationTracker application")
    }
}