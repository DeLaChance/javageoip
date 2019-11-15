package nl.lucien.adapter;

import lombok.extern.slf4j.Slf4j;
import nl.lucien.BaseIntegrationTest;
import nl.lucien.TestUtils;
import nl.lucien.domain.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.http.HttpResponse;

import static java.lang.String.format;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@Slf4j
public class UserControllerIT extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private UserDto existingUser;

    @Before
    public void beforeEachTest() {
        clearUserRepository();

        existingUser = TestUtils.existingUser();
        userRepository.insert(existingUser).block();
    }

    @After
    public void afterEachTest() {
        clearUserRepository();
    }

    @Test
    public void test_that_an_existing_user_can_be_found() throws IOException {
        // Given
        String url = generateUserControllerUrl() + "/" + existingUser.getId();

        // When
        HttpResponse<String> response = performGetCall(url);

        // Then
        assertTrue(response.statusCode() == HttpStatus.OK.value());

        UserDto returnedUser = OBJECT_MAPPER.readValue(response.body(), UserDto.class);
        assertThat(returnedUser, is(equalTo(existingUser)));
    }

    @Test
    public void test_that_a_new_user_can_be_created() {
        // Given
        String url = generateUserControllerUrl();
        UserDto user = TestUtils.newUser();

        // When
        HttpResponse<String> response = peformPostCall(user, url);

        // Then
        assertTrue(response.statusCode() == HttpStatus.CREATED.value());
    }

    @Test
    public void test_that_an_existing_user_can_be_updated() throws IOException {
        // Given
        existingUser.getKeywords().add("Master of whisperers");
        String url = generateUserControllerUrl() + "/" + existingUser.getId();

        // When
        HttpResponse<String> response = peformPutCall(existingUser, url);

        // Then
        assertTrue(response.statusCode() == HttpStatus.OK.value());

        UserDto returnedUser = OBJECT_MAPPER.readValue(response.body(), UserDto.class);
        assertThat(returnedUser, is(equalTo(existingUser)));
    }

    @Test
    public void test_that_an_existing_user_can_be_deleted() throws IOException {
        // Given
        existingUser.getKeywords().add("Master of whisperers");
        String url = generateUserControllerUrl() + "/" + existingUser.getId();

        // When
        HttpResponse<String> response = peformDeleteCall(url);

        // Then
        assertTrue(response.statusCode() == HttpStatus.OK.value());

        response = performGetCall(url);
        assertTrue(response.statusCode() == HttpStatus.NOT_FOUND.value());
    }

    public String generateUserControllerUrl() {
        return format("http://localhost:%s/api/users", serverPort);
    }

    private void clearUserRepository() {
        userRepository.findAll().toStream()
                .forEach(user -> userRepository.deleteByUserId(user.getId()).block());
    }
}
