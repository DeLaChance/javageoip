package nl.lucien.adapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import nl.lucien.domain.PathMetaData;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = PathMetadataDto.Builder.class)
@Builder(builderClassName = "Builder")
@Data
public class PathMetadataDto {

    private final String pathId;
    private final String userId;
    private final Integer length;

    public static PathMetadataDto from(PathMetaData pathMetaData) {
        return PathMetadataDto.builder()
            .pathId(pathMetaData.getId())
            .userId(pathMetaData.getUserId())
            .length(pathMetaData.getLength())
            .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
    }
}
