package nl.lucien.adapter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import nl.lucien.domain.Location;

@JsonDeserialize(builder = LocationDto.Builder.class)
@Builder
@Data
public class LocationDto {

    private Double longitude;
    private Double latitude;
    private Long timestamp;

    public static LocationDto from(Location location) {
        return LocationDto.builder()
            .longitude(location.getLongitude())
            .latitude(location.getLatitude())
            .timestamp(location.getTimestamp())
            .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
