package nl.lucien.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.r2dbc.spi.Row;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Represents a point on Earth at a certain time.
 */
@JsonDeserialize(builder = Location.Builder.class)
@Builder(builderClassName = "Builder")
@Data
public class Location {

    public static final Double MIN_LATITUDE = -90.0;
    public static final Double MAX_LATITUDE = 90.0;
    public static final Double MIN_LONGITUDE = -180.0;
    public static final Double MAX_LONGITUDE = 180.0;

    private String id;
    private Double longitude;
    private Double latitude;
    private Long timestamp; // unix timestamp (seconds since epoch)

    public static Location randomLocation() {
        Random random = new Random();
        double latitude = (random.nextDouble() % MAX_LATITUDE) - MAX_LATITUDE;
        double longitude = (random.nextDouble() % MAX_LONGITUDE) - MAX_LONGITUDE;

        Location randomLocation = Location.builder()
            .id(UUID.randomUUID().toString())
            .longitude(longitude)
            .latitude(latitude)
            .timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
            .build();

        return randomLocation;
    }

    public static Location from(Row row) {
        Double longitude = fetchAttributeFromRow("longitude", row)
                .map(Double::parseDouble).orElse(null);
        Double latitude = fetchAttributeFromRow("latitude", row)
                .map(Double::parseDouble).orElse(null);
        Long unixTimestamp = fetchAttributeFromRow("timestamp", row)
                .map(Long::parseLong)
                .orElse(null);

        Location location = Location.builder()
            .latitude(latitude)
            .longitude(longitude)
            .timestamp(unixTimestamp)
            .build();

        return location;
    }

    private static Optional<String> fetchAttributeFromRow(String attribute, Row row) {
        Object object = row.get(attribute);
        return Optional.ofNullable(object)
            .map(String::valueOf)
            .filter(Predicate.not(String::isEmpty));
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
