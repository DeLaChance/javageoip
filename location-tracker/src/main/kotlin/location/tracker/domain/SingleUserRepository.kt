package location.tracker.domain

interface SingleUserRepository {
    fun fetchUser(): User?
}