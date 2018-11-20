package nl.cloud.location.domain.user

interface UserRepository {
    fun fetchUsers(): List<User>
    fun findUserById(id: String): User?
    fun addUser(user: User): User
    fun deleteUserById(id: String): User?
}