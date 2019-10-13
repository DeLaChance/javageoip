package nl.lucien.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import nl.lucien.adapter.UserDto;

import java.util.List;
import java.util.UUID;

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

    public static User from(UserDto userInput) {
        return User.builder()
            .id(UUID.randomUUID().toString())
            .name(userInput.getName())
            .keyWords(userInput.getKeywords())
            .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
