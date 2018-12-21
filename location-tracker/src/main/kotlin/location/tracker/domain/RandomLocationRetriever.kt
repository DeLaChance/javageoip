package location.tracker.domain

import io.micronaut.context.annotation.Primary
import io.micronaut.scheduling.annotation.Scheduled
import org.slf4j.LoggerFactory
import java.time.Instant
import javax.inject.Singleton
import kotlin.random.Random

@Primary
@Singleton
class RandomLocationRetriever : LocationRetriever {

    private val logger = LoggerFactory.getLogger(RandomLocationRetriever::class.java)

    var location: TimedLocation = TimedLocation(51.44, 5.47,
            currentTimeStamp())

    @Scheduled(fixedDelay = "10s")
    fun moveLocationRandomly() {
        var longitudeDelta: Double = Random.nextDouble(-0.005, 0.01)
        var latitudeDelta: Double = Random.nextDouble(-0.01, 0.01)

        var newLocation = TimedLocation(
                location.longitude + longitudeDelta,
            location.latitude + latitudeDelta,
                currentTimeStamp()
            )


        logger.info("Updated time and location of this device to ${newLocation}")

        location = newLocation
    }

    fun currentTimeStamp(): Long {
        return Instant.now().toEpochMilli()
    }

    override fun retrieveTimedLocation(): TimedLocation {
        return location
    }

}