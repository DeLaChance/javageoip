package nl.lucien.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.r2dbc.spi.Row;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Represents a chain of {@link Location}'s traversed by a {@link User}.
 */
@JsonDeserialize(builder = Path.Builder.class)
@Builder
@Data
public class Path {

    private PathMetaData pathMetadata;
    private List<Location> locations;

    public static Path from(PathMetaData pathMetaData, List<Location> locations) {
        return Path.builder()
            .pathMetadata(pathMetaData)
            .locations(locations)
            .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}

