package nl.cloud.location.domain.user

import io.micronaut.context.annotation.Primary
import java.util.*
import javax.inject.Singleton

@Primary
@Singleton
class InMemoryUserRepository : UserRepository {

    private val users: HashMap<String, User> = hashMapOf()

    init {
        this.addUser(User("John Snow"))
    }

    override fun fetchUsers(): List<User> {
        return users.values.toList()
    }

    override fun findUserById(id: String): User? {
        return this.users.getOrDefault(id, null)
    }

    override fun addUser(user: User): User {
        this.users.put(user.id.value, user)
        return user
    }

    override fun deleteUserById(id: String): User? {
        return this.users.remove(id)
    }

}