package location.tracker.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Value
import org.slf4j.LoggerFactory
import java.io.File
import javax.annotation.PostConstruct
import javax.inject.Singleton



/**
 * A single-value user repository {@link User} backed by the file system.
 */
@Primary
@Singleton
class FileBasedSingleUserRepository : SingleUserRepository {

    private val logger = LoggerFactory.getLogger(FileBasedSingleUserRepository::class.java)

    @Value("\${micronaut.user.location}")
    lateinit var fileLocation: String

    @Value("\${micronaut.user.default-location}")
    lateinit var defaultFileLocation: String

    private val mapper = ObjectMapper().registerModule(KotlinModule())

    var user: User? = null
        private set(user) {
            logger.info("Set user to {}", user)
            field = user
        }

    @PostConstruct
    fun readUserFile() {
        val jsonString: String

        if( File(fileLocation).exists() ) {
            logger.info("Reading user file at location: {}", fileLocation)

            jsonString = File(fileLocation)
                .inputStream()
                .readBytes()
                .toString(Charsets.UTF_8)
        } else {
            fileLocation = defaultFileLocation
            logger.info("Reading user file from class path at default location: {}", fileLocation)
            jsonString = ClassLoader.getSystemResourceAsStream(fileLocation)
                .readBytes()
                .toString(Charsets.UTF_8)
        }

        val newUser: User? = mapper.readValue<User>(jsonString)
        if ( newUser != null ) {
            user = newUser
        }
    }

    override fun fetchUser(): User? {
        return user
    }
}
