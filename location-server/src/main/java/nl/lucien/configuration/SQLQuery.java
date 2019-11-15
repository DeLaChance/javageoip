package nl.lucien.configuration;

import liquibase.util.StringUtils;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Getter
public class SQLQuery {

    private final String sqlExpression;
    private final Map<String, Object> parameters;

    private SQLQuery(String sqlExpression, Map<String, Object> parameters) {
        this.sqlExpression = sqlExpression;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        if (StringUtils.isNotEmpty(sqlExpression) && parameters != null) {
            String sql = sqlExpression.replaceAll("\\$[0-9]+", "%s");
            String[] parametersArray = parameters.values()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.toList())
                .toArray(new String[]{});

            return format(sql, parametersArray);
        } else {
            return sqlExpression;
        }
    }

    public static SQLQuery from(String sqlExpression) {
        return from(sqlExpression, new ArrayList<>());
    }

    public static SQLQuery from(String sqlExpression, Object... parameters) {
        return from(sqlExpression, Arrays.asList(parameters));
    }

    public static SQLQuery from(String sqlExpression, List<Object> parameters) {

        Map<String, Object> parameterMap = new LinkedHashMap<>();

        int index = 1;
        for (Object value : parameters) {
            String key = format("$%s", index);
            parameterMap.put(key, value);
            index += 1;
        }

        return new SQLQuery(sqlExpression, parameterMap);
    }
}
