package nl.lucien.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a point on Earth.
 */
@JsonDeserialize(builder = Path.Builder.class)
@Builder
@Data
public class Location {

    public static Double MIN_LATITUDE = -90.0;
    public static Double MAX_LATITUDE = 90.0;
    public static Double MIN_LONGITUDE = -180.0;
    public static Double MAX_LONGITUDE = 180.0;

    private Double longitude;
    private Double latitude;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
