package nl.cloud.location.domain

import java.util.*

/**
 * Represents a user entity in the application.
 */
class User(val name: String)
{
    val id: String = UUID.randomUUID().toString()
    val keyWords: MutableList<String> = mutableListOf()
    val timedLocations: MutableList<TimedGeoLocation> = mutableListOf()

    fun addKeyword(keyword: String): User {
        keyWords.add(keyword)

        return this
    }

    fun timedLocations(timedLocation: TimedGeoLocation): User {
        timedLocations.add(timedLocation)

        return this
    }
}