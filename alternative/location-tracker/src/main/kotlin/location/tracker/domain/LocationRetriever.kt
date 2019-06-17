package location.tracker.domain

/**
 * Retrieves a timed location of this device.
 */
interface LocationRetriever {

    fun retrieveTimedLocation(): TimedLocation

}