package nl.lucien.configuration;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@Getter
public class SQLQuery {

    private final String sqlExpression;
    private final Map<String, String> parameters;

    private SQLQuery(String sqlExpression, Map<String, String> parameters) {
        this.sqlExpression = sqlExpression;
        this.parameters = parameters;
    }

    public static SQLQuery from(String sqlExpression, String... parameters) {

        Map<String, String> parameterMap = new HashMap<>();
        int index = 1;
        for (String value : parameters) {
            String key = format("$%s", index);
            parameterMap.put(key, value);
            index += 1;
        }

        return new SQLQuery(sqlExpression, parameterMap);
    }
}
