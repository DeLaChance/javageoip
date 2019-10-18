package nl.lucien.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.r2dbc.spi.Row;
import nl.lucien.adapter.UserDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

/**
 * Represents someone using this app.
 */
@JsonDeserialize(builder = User.Builder.class)
@Builder
@Data
public class User {

    private String id;
    private String name;
    private List<String> keyWords;

    public static User from(UserDto userInput) {
        return User.builder()
            .id(userInput.getId())
            .name(userInput.getName())
            .keyWords(userInput.getKeywords())
            .build();
    }

    public static User from(Row row) {
        String keywordsString = row.get("keywords", String.class);

        List<String> keyWords = new ArrayList<>();
        if (!StringUtils.isEmpty(keywordsString)) {
            String[] keyWordsArray = keywordsString.replaceAll("\\[\\]", "")
                .split(",");

            if (keyWordsArray != null) {
                keyWords.addAll(Arrays.asList(keyWordsArray));
            }
        }

        return User.builder()
            .id(row.get("id", String.class))
            .name(row.get("name", String.class))
            .keyWords(keyWords)
            .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
