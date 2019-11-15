package nl.lucien.adapter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonDeserialize(builder = UserDto.Builder.class)
public class UserDto {

    private String id;
    private String name;
    private List<String> keywords;

    UserDto(String id, String name, List<String> keywords) {
        this.id = id;
        this.name = name;
        this.keywords = keywords;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String keywordsAsString() {
        if (keywords == null) {
            return null;
        } else {
            return keywords.stream().collect(Collectors.joining(","));
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String id;
        private String name;
        private List<String> keywords;

        Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder keywords(List<String> keywords) {
            this.keywords = keywords;
            return this;
        }

        public UserDto build() {
            return new UserDto(id, name, keywords);
        }
    }
}
