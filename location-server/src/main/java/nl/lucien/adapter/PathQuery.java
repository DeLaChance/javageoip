package nl.lucien.adapter;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Builder
@Getter
public class PathQuery {

    private String userId;
    private Long startTime;
    private Long endTime;
    private Integer pageSize;
    private Integer pageNumber;

    public boolean isValid() {
        boolean isValid = !StringUtils.isEmpty(userId);

        if (startTime != null && endTime != null) {
            isValid &= startTime <= endTime;
        }

        isValid &= (pageSize == null || pageSize >= 0);
        isValid &= (pageNumber == null || pageNumber >= 0);

        return isValid;
    }
}
