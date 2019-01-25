package nl.cloud.location.domain.user

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import javax.annotation.PostConstruct
import javax.inject.Singleton

@Singleton
class InMemoryUserRepository : UserRepository {

    val logger: Logger = LoggerFactory.getLogger(InMemoryUserRepository::class.qualifiedName)

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
        logger.info("Added user ${user} to repo")

        return user
    }

    override fun deleteUserById(id: String): User? {
        logger.info("Removed user with id ${id} from repo")
        return this.users.remove(id)
    }

}