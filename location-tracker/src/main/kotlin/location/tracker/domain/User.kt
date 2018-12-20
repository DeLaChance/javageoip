package location.tracker.domain

import java.util.*

/**
 * The user of this location-tracker application
 */
class User(
    val id: UUID,
    val name: String,
    val keyWords: List<String>
) {

    init {
        if( this.name == "" ) {
            throw IllegalArgumentException("${this.name} is not a valid name")
        }
    }

    companion object {
        fun from(userDto: UserDto): User {
            return User(UUID.randomUUID(), userDto.name, userDto.keyWords)
        }
    }

    override fun toString(): String {
        return "{ id=${id}, name=${name}, keyWords=[${keyWords}] }"
    }

}
