package nl.lucien.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.r2dbc.spi.Row;
import lombok.Builder;
import lombok.Data;

/**
 * Holds all non-geografic data about a {@link Path}.
 */
@JsonDeserialize(builder = PathMetaData.Builder.class)
@Builder(builderClassName = "Builder")
@Data
public class PathMetaData {

    private String id;
    private String userId;
    private Integer length;

    public static PathMetaData from(Row row) {
        String id = row.get("id", String.class);
        String userId = row.get("userid", String.class);

        Object lengthObject = row.get("length");
        Integer length = (lengthObject == null) ? 0 : Integer.parseInt(String.valueOf(lengthObject));

        return PathMetaData.builder()
            .id(id)
            .userId(userId)
            .length(length)
            .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
