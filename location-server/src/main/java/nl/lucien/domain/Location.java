package nl.lucien.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.r2dbc.spi.Row;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a point on Earth at a certain time.
 */
@JsonDeserialize(builder = Location.Builder.class)
@Builder
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

    private Location from(Row row) {
        Double longitude = row.get("longitude", Double.class);
        Double latitude = row.get("latitude", Double.class);
        Long unixTimestamp = row.get("timestamp", Long.class);

        Location location = Location.builder()
                .latitude(latitude)
                .longitude(longitude)
                .timestamp(unixTimestamp)
                .build();

        return location;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
