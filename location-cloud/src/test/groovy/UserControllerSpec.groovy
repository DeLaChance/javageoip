import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import nl.cloud.location.domain.user.User
import nl.cloud.location.domain.user.UserId
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class UserControllerSpec extends Specification {

    @Shared
    @AutoCleanup
    private EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer, "integrationTest", "inMemory")

    @Shared
    @AutoCleanup
    private HttpClient client = HttpClient.create(embeddedServer.URL)

    void "test that when the hello api is invoked that a hello-echo response is returned"() {
        expect:
        client.toBlocking()
                .retrieve(HttpRequest.GET('/api/users/hello/Lucien')) == "Hello Lucien"
    }

    void "test that when the fetch users api is invoked that the list of users is returned as a response"() {
        expect:
        List<User> users = client.toBlocking().retrieve(HttpRequest.GET('/api/users/'), List.class)

        users.size() == 1
        users[0].name == "John Snow"
    }

    void "test that when the fetch user by id api is invoked that the found user is returned"() {
        given:
        List<User> users = client.toBlocking().retrieve(HttpRequest.GET('/api/users/'), Argument.of(List.class, User.class))
        UserId id = users[0].id

        when:
        User user = client.toBlocking().retrieve(HttpRequest.GET("/api/users/${id}"), User.class)

        then:
        user.id == id
    }

    void "test that when the create user api is invoked that the list of users contains that user"() {
        given:
        User user = new User("Eddard Stark")

        when:
        def returnedUser = client.toBlocking().retrieve(HttpRequest.POST("/api/users/", user), User.class)

        then:
        returnedUser.name == "Eddard Stark"
    }

    void "test that when the delete user api is invoked that the found user is deleted"() {
        given:
        List<User> users = client.toBlocking().retrieve(HttpRequest.GET('/api/users/'), Argument.of(List.class, User.class))
        UserId id = users[0].id

        when:
        HttpResponse<User> response = client.toBlocking().exchange(HttpRequest.DELETE("/api/users/${id}"))

        then:
        response.status() == HttpStatus.OK
    }
}
