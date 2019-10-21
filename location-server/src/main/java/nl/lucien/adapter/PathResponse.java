package nl.lucien.adapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import nl.lucien.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

@JsonDeserialize(builder = PathResponse.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class PathResponse {

    private final String pathId;
    private final String userId;
    private final Long startTime;
    private final Long endTime;
    private final Integer pageSize;
    private final Integer pageNumber;
    private final Integer maxNumberOfPages;
    private List<LocationDto> geolocations;

    public static PathResponse from(Path path) {
        return PathResponse.builder()
            .pathId(path.getId())
            .userId(path.getUserId())
            .geolocations(path.getLocations()
                .stream()
                .map(LocationDto::from)
                .collect(Collectors.toList())
            )
            .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
