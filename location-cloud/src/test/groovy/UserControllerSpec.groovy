import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import nl.cloud.location.domain.user.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Timeout

class UserControllerSpec extends Specification {

    @Shared
    private Logger logger = LoggerFactory.getLogger(UserControllerSpec.class)

    @Shared
    @AutoCleanup
    private EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer, "integrationTest", "inMemory")

    @Shared
    @AutoCleanup
    private HttpClient client = HttpClient.create(embeddedServer.URL)

    def setupSpec() {
        logger.info("Started up embedded server with url {} ", embeddedServer.URL.toString())
    }

    @Timeout(value = 1)
    void "test that when the hello api is invoked that a hello-echo response is returned"() {
        expect:
        client.toBlocking()
                .retrieve(HttpRequest.GET('/api/users/hello/Lucien')) == "Hello Lucien"
    }

    @Timeout(value = 1)
    void "test that when the fetch users api is invoked that the list of users is returned as a response"() {
        expect:
        List<User> users = client.toBlocking().retrieve(HttpRequest.GET('/api/users/'), List.class)

        users.size() == 1
        users[0].name == "John Snow"
    }

    @Timeout(value = 1)
    void "test that when the fetch user by id api is invoked that the found user is returned"() {
        given:
        List<User> users = client.toBlocking().retrieve(HttpRequest.GET('/api/users/'), Argument.of(List.class, User.class))
        String id = users[0].id

        when:
        User user = client.toBlocking().retrieve(HttpRequest.GET("/api/users/${id}"), User.class)

        then:
        user.id == id
    }

    @Timeout(value = 1)
    void "test that when the create user api is invoked that the list of users contains that user"() {
        given:
        User user = new User("Eddard Stark")

        when:
        def returnedUser = client.toBlocking().retrieve(HttpRequest.POST("/api/users/", user), User.class)

        then:
        returnedUser.name == "Eddard Stark"
    }

    @Timeout(value = 1)
    void "test that when the delete user api is invoked that the found user is deleted"() {
        given:
        List<User> users = client.toBlocking().retrieve(HttpRequest.GET('/api/users/'), Argument.of(List.class, User.class))
        String id = users[0].id

        when:
        HttpResponse<User> response = client.toBlocking().exchange(HttpRequest.DELETE("/api/users/${id}"))

        then:
        response.status() == HttpStatus.OK
    }
}
