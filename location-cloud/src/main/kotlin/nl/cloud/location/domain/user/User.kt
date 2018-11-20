package nl.cloud.location.domain.user

/**
 * Represents a user entity in the application.
 */
class User(val name: String)
{
    val id: UserId = UserId.generate()
    val keyWords: MutableList<String> = mutableListOf()

    fun addKeyword(keyword: String): User {
        keyWords.add(keyword)

        return this
    }
}