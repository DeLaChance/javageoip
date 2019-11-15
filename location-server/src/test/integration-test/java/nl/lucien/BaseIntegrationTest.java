package nl.lucien;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;
import static io.netty.handler.codec.http.HttpMethod.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
@Slf4j
public abstract class BaseIntegrationTest {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    protected static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .build();

    @LocalServerPort
    protected int serverPort;

    public HttpResponse<String> performGetCall(String url) {
        return httpCall(GET, url);
    }

    public HttpResponse<String> peformPostCall(Object entity, String url) {
        return httpCall(HttpMethod.POST, entity, url);
    }

    public HttpResponse<String> peformPutCall(Object entity, String url) {
        return httpCall(PUT, entity, url);
    }

    public HttpResponse<String> peformDeleteCall(String url) {
        return httpCall(DELETE, url);
    }

    private HttpResponse<String> httpCall(HttpMethod method, String url) {
        try {
            log.info("Sending {}-request to url '{}'", method.name(), url);

            HttpRequest request = buildRequest(method, url, null);
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("Response status code is '{}' with message '{}'", response.statusCode(), response.body());

            return response;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Integration test has failed due to: ", e);
        }
    }

    private HttpResponse<String> httpCall(HttpMethod method, Object entity, String url) {
        try {

            String entityString = entityToString(entity);

            log.info("Sending {}-request to url '{}' with payload '{}'", method.name(), url, entityString);

            HttpRequest request = buildRequest(method, url, entityString);
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("Response status code is '{}' with message '{}'", response.statusCode(), response.body());

            return response;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Integration test has failed due to: ", e);
        }
    }

    private HttpRequest buildRequest(HttpMethod method, String url, String entityString) {
        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .timeout(Duration.ofSeconds(2));

        if (method == GET) {
            httpRequestBuilder = httpRequestBuilder.GET();
        } else if (method == DELETE) {
            httpRequestBuilder = httpRequestBuilder.DELETE();
        } else if (method == PUT) {
            httpRequestBuilder = httpRequestBuilder.PUT(HttpRequest.BodyPublishers.ofString(entityString));
        } else if (method == POST) {
            httpRequestBuilder = httpRequestBuilder.POST(HttpRequest.BodyPublishers.ofString(entityString));
        } else {
            throw new IllegalArgumentException("Invalid method");
        }

        return httpRequestBuilder.build();
    }

    public String entityToString(Object entity) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(entity);
    }
}
