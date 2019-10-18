package nl.lucien.adapter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserDto {

    private String id;
    private String name;
    private List<String> keywords;

    public String keywordsAsString() {
        if (keywords == null) {
            return null;
        } else {
            return keywords.stream().collect(Collectors.joining(", "));
        }
    }
}
