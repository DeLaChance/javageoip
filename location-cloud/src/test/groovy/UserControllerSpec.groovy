import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import nl.cloud.location.domain.User
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class UserControllerSpec extends Specification {

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    @Shared
    @AutoCleanup
    HttpClient client = HttpClient.create(embeddedServer.URL)

    void "test that when the hello api is invoked that a hello-echo response is returned"() {
        expect:
        client.toBlocking()
                .retrieve(HttpRequest.GET('/api/users/hello/Lucien')) == "Hello Lucien"
    }

    void "test that when the fetch users api is invoked that the list of users is returned as a response"() {
        given:
        def objectMapper = new ObjectMapper()

        expect:
        List<User> users = client.toBlocking()
            .retrieve(HttpRequest.GET('/api/users/'), List.class)

        users.size() == 1
        users[0].name == "John Snow"
    }

    void "test that when the create user api is invoked that the list of users contains that user"() {
        given:
        User user = new User("Eddard Stark")

        when:
        def returnedUser = client.toBlocking().retrieve(HttpRequest.POST("/api/users/", user), User.class)

        then:
        returnedUser.name == "Eddard Stark"

        List<User> users = client.toBlocking().retrieve(HttpRequest.GET("/api/users"), List.class)
        users.size() == 2
        users[1].name == "Eddard Stark"
    }
}
