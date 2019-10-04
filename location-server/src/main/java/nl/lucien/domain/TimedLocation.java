package nl.lucien.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a point on Earth at a certain time.
 */
@JsonDeserialize(builder = Path.Builder.class)
@Builder
@Data
public class TimedLocation {

    private Location location;
    private Long timestamp; // Unix timestamp (ms)

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
