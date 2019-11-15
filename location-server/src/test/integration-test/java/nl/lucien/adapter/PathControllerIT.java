package nl.lucien.adapter;

import lombok.extern.slf4j.Slf4j;
import nl.lucien.BaseIntegrationTest;
import nl.lucien.TestUtils;
import nl.lucien.domain.Path;
import nl.lucien.domain.PathRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@Slf4j
public class PathControllerIT extends BaseIntegrationTest {

    @Autowired
    private PathRepository pathRepository;

    @Autowired
    private UserRepository userRepository;

    private UserDto existingUser;
    private Path existingPath;

    @Before
    public void beforeEachTest() throws InterruptedException {
        clearRepositories();

        existingUser = TestUtils.existingUser();
        userRepository.insert(existingUser).block();

        existingPath = pathRepository.createPathFor(existingUser.getId()).block();
        pathRepository.addLocationToPath(existingPath.getPathMetadata().getId(), TestUtils.newLocation()).block();
        pathRepository.addLocationToPath(existingPath.getPathMetadata().getId(), TestUtils.newLocation()).block();
        pathRepository.addLocationToPath(existingPath.getPathMetadata().getId(), TestUtils.newLocation()).block();
    }

    @After
    public void afterEachTest() {
        clearRepositories();
    }

    @Test
    public void test_that_a_new_path_can_be_created_for_an_existing_user() throws IOException {
        // Given
        String url = generatePathControllerUrl() + "/users/" + existingUser.getId();

        // When
        HttpResponse<String> response = performPostCall(null, url);

        // Then
        assertTrue(response.statusCode() == HttpStatus.CREATED.value());

        Path returnedEntity = OBJECT_MAPPER.readValue(response.body(), Path.class);
        assertThat(returnedEntity.getPathMetadata().getLength(), is(equalTo(0)));
        assertThat(returnedEntity.getPathMetadata().getUserId(), is(equalTo(existingUser.getId())));
    }

    @Test
    public void test_that_existing_path_can_be_found() {
        // Given
        String url = generatePathControllerUrl() + "/" + existingPath.getPathMetadata().getId();

        // When
        HttpResponse<String> response = performGetCall(url);

        // Then
        assertTrue(response.statusCode() == HttpStatus.OK.value());
    }

    @Test
    public void test_that_locations_can_be_added_to_existing_path() {
        // Given
        String url = generatePathControllerUrl() + "/" + existingPath.getPathMetadata().getId();
        LocationDto entity = TestUtils.newLocation();

        // When
        HttpResponse<String> response = performPostCall(entity, url);

        // Then
        assertTrue(response.statusCode() == HttpStatus.OK.value());
    }

    @Test
    public void test_that_paths_can_be_queried_by_user() throws IOException {
        // Given
        String url = generatePathControllerUrl() + "?userId=" + existingUser.getId();

        // When
        HttpResponse<String> response = performGetCall(url);

        // Then
        assertTrue(response.statusCode() == HttpStatus.OK.value());

        log.info("Path query response: {}", response.body());

        List<PathResponse> pathResponseList = Arrays.asList(OBJECT_MAPPER.readValue(response.body(), PathResponse[].class));
        assertThat(pathResponseList.size(), is(equalTo(1)));

        PathResponse pathResponse = pathResponseList.get(0);
        assertThat(pathResponse, is(notNullValue()));
        assertThat(pathResponse.getPathMetadata().getPathId(), is(equalTo(existingPath.getPathMetadata().getId())));
        assertThat(pathResponse.getGeolocations().size(), is(equalTo(3)));
    }

    public String generatePathControllerUrl() {
        return format("http://localhost:%s/api/paths", serverPort);
    }

    private void clearRepositories() {
        userRepository.findAll().toStream()
            .forEach(user -> userRepository.deleteByUserId(user.getId()).block());

        pathRepository.findAll().toStream()
            .forEach(path -> pathRepository.deleteByPathId(path.getPathMetadata().getId()));
    }
}
