package nl.lucien.adapter;

import nl.lucien.domain.Location;
import nl.lucien.domain.Path;
import nl.lucien.domain.PathMetaData;
import nl.lucien.domain.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.*;
import java.util.*;

@RestController
@RequestMapping(value = "/dummy", produces = MediaType.APPLICATION_JSON_VALUE)
public class DummyController {

    private static User JOHN_SNOW = User.builder()
        .id(UUID.randomUUID().toString())
        .name("John Snow")
        .keyWords(Arrays.asList("Lord Commander", "Azor Ahai"))
        .build();

    private static User TYRION_LANNISTER = User.builder()
        .id(UUID.randomUUID().toString())
        .name("Tyrion Lannister")
        .keyWords(Arrays.asList("Imp", "Halfman"))
        .build();

    private static Location EINDHOVEN_LOCATION = Location.builder()
        .latitude(51.44)
        .longitude(5.47)
        .build();

    private static Location HELMOND_LOCATION = Location.builder()
        .latitude(51.28)
        .longitude(5.39)
        .build();

    @GetMapping("/users")
    public Flux<User> getUsers() {
        return Flux.just(JOHN_SNOW, TYRION_LANNISTER);
    }

    @GetMapping("/paths")
    public Flux<Path> getAllPaths() {
        return Flux.just(generatePath(EINDHOVEN_LOCATION, JOHN_SNOW, 10),
            generatePath(HELMOND_LOCATION, TYRION_LANNISTER, 20));
    }

    private static Path generatePath(Location startLocation, User user, int pathLength) {
        Random random = new Random();
        List<Location> locations = new ArrayList<>();

        Location oldLocation = startLocation;
        for (int i = 0; i < pathLength; i++) {
            locations.add(oldLocation);

            double horizontalOffset = (random.nextDouble() % 0.2) - 0.1;
            double verticalOffset = (random.nextDouble() % 0.2) - 0.1;
            Location newLocation = Location.builder()
                .id(UUID.randomUUID().toString())
                .longitude(oldLocation.getLongitude() - horizontalOffset)
                .latitude(oldLocation.getLatitude() - verticalOffset)
                .timestamp(LocalDateTime.now().minusDays(3).toEpochSecond(ZoneOffset.UTC) + i * 3600L)
                .build();

            oldLocation = newLocation;
        }

        return Path.builder()
            .pathMetadata(PathMetaData.builder()
                .id(UUID.randomUUID().toString())
                .userId(user.getId())
                .length(pathLength)
                .build())
            .locations(locations)
            .build();
    }
}
