package nl.lucien.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
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

    private String id;
    private User user;
    private List<Location> locations;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}

