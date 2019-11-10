package nl.lucien.configuration;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@Getter
public class SQLQuery {

    private final String sqlExpression;
    private final Map<String, Object> parameters;

    private SQLQuery(String sqlExpression, Map<String, Object> parameters) {
        this.sqlExpression = sqlExpression;
        this.parameters = parameters;
    }

    public static SQLQuery from(String sqlExpression, Object... parameters) {
        return from(sqlExpression, Arrays.asList(parameters));
    }

    public static SQLQuery from(String sqlExpression, List<Object> parameters) {

        Map<String, Object> parameterMap = new HashMap<>();

        int index = 1;
        for (Object value : parameters) {
            String key = format("$%s", index);
            parameterMap.put(key, value);
            index += 1;
        }

        return new SQLQuery(sqlExpression, parameterMap);
    }
}
