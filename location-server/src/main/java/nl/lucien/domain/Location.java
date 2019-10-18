package nl.lucien.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;

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

    @Id
    private String id;
    private Double longitude;
    private Double latitude;
    private ZonedDateTime timestamp;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
