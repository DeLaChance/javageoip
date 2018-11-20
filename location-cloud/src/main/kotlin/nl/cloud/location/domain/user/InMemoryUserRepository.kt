package nl.cloud.location.domain.user

import java.util.*
import javax.annotation.PostConstruct

class InMemoryUserRepository : UserRepository {

    private val users: HashMap<String, User> = hashMapOf()

    @PostConstruct
    fun postConstruct() {
        println("Started up InMemoryUserRepository")
    }

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