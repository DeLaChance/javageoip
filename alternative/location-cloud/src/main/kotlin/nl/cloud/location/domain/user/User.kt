package nl.cloud.location.domain.user

/**
 * Represents a user entity in the application.
 */
class User(
        val id: String = UserId.generate().value,
        val name: String,
        val keyWords: MutableList<String> = mutableListOf()
) {
    // Constructor for non-Kotlin code, e.g. the Spock unit tests.
    constructor(name: String) : this(UserId.generate().value, name, mutableListOf())

    init {
        if( this.id.isEmpty() ) {
            throw IllegalArgumentException("${this.id} is not a valid id")
        }

        if( this.name.isEmpty() ) {
            throw IllegalArgumentException("${this.name} is not a valid name")
        }
    }

    fun addKeyword(keyWord: String) {
        keyWords.add(keyWord)
    }

    override fun toString(): String {
        return "{ id=${id}, name=${name}, keyWords=[${keyWords}] }"
    }

}