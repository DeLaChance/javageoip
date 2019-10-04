package nl.lucien.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Represents someone using this app.
 */
@JsonDeserialize(builder = Path.Builder.class)
@Builder
@Data
public class User {

    private String id;
    private String name;
    private List<String> keyWords;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
