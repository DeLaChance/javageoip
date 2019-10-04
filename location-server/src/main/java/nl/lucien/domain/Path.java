package nl.lucien.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Represents a chain of {@link TimedLocation}'s traversed by a {@link User}.
 */
@JsonDeserialize(builder = Path.Builder.class)
@Builder
@Data
public class Path {

    private User user;
    private List<TimedLocation> locations;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
