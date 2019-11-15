package nl.lucien;

import nl.lucien.adapter.UserDto;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUtils {

    private TestUtils() {
    }

    public static UserDto newUser() {
        return UserDto.builder()
            .id(UUID.randomUUID().toString())
            .name("John Snow")
            .keywords(Stream.of("Lord Commander", "Azor Ahai").collect(Collectors.toList()))
            .build();
    }

    public static UserDto existingUser() {
        return UserDto.builder()
            .id(UUID.randomUUID().toString())
            .name("Varys")
            .keywords(Stream.of("Eunuch", "Spider").collect(Collectors.toList()))
            .build();
    }
}
