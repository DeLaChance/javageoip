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
        val dummyUser: User = User(name="John Snow")
        this.addUser(dummyUser)
    }

    override fun fetchUsers(): List<User> {
        return users.values.toList()
    }

    override fun findUserById(id: String): User? {
        return this.users.getOrDefault(id, null)
    }

    override fun addUser(user: User): User {
        this.users.put(user.id, user)
        return user
    }

    override fun deleteUserById(id: String): User? {
        return this.users.remove(id)
    }

}