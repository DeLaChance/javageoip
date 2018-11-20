package nl.cloud.location.domain.user

import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Secondary

/**
 * The factory can generate different types of {@link UserRepository}.
 */
@Factory
class UserFactory {

    @Requires(notEnv = ["inMemory"])
    @Primary
    fun databaseUserRepository(): UserRepository {
        TODO("Implement this repository")
    }

    @Requires(env = ["inMemory"])
    @Secondary
    fun inMemoryUserRepository(): UserRepository {
        return InMemoryUserRepository()
    }

}