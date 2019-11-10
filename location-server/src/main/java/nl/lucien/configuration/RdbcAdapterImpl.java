package nl.lucien.configuration;


import io.r2dbc.spi.*;
import lombok.extern.slf4j.Slf4j;
import nl.lucien.domain.Location;
import nl.lucien.domain.Path;
import nl.lucien.domain.PathMetaData;
import nl.lucien.domain.User;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@Component
@Slf4j
public class RdbcAdapterImpl implements RdbcAdapter {

    private ConnectionFactory connectionFactory;
    private Map<Class<?>, Method> map;

    @Autowired
    public RdbcAdapterImpl(ConnectionFactory connectionFactory) throws NoSuchMethodException {
        this.connectionFactory = connectionFactory;

        databaseEntityScan();
    }

    @Override
    public <T> Flux<T> findAll(SQLQuery sqlQuery, Class<T> klass) {
        return runSQLQuery(sqlQuery)
            .flatMap(result -> createEntityFrom(klass, result));
    }

    @Override
    public <T> Mono<T> find(SQLQuery sqlQuery, Class<T> klass) {
        return runSQLQuery(sqlQuery)
            .next()
            .flatMap(result -> Mono.from(createEntityFrom(klass, result)));
    }

    @Override
    public Mono<Boolean> insert(SQLQuery insertQuery) {
        return executeWriteQuery(insertQuery);
    }

    @Override
    public Mono<Boolean> update(SQLQuery sqlQuery) {
        return executeWriteQuery(sqlQuery);
    }

    @Override
    public Mono<Boolean> delete(SQLQuery sqlQuery) {
        return executeWriteQuery(sqlQuery);
    }

    private void databaseEntityScan() throws NoSuchMethodException {
        // TODO: replace with real annotation based scan
        map = new HashMap<>();
        map.put(User.class, User.class.getDeclaredMethod("from", Row.class));
        map.put(PathMetaData.class, PathMetaData.class.getDeclaredMethod("from", Row.class));
        map.put(Location.class, Location.class.getDeclaredMethod("from", Row.class));
    }

    private Mono<Boolean> executeWriteQuery(SQLQuery sqlQuery) {
        return runSQLQuery(sqlQuery)
            .next()
            .flatMap(row -> Mono.from(row.getRowsUpdated()))
            .map(rowsUpdated -> rowsUpdated == 1);
    }

    private Mono<Connection> createConnection() {
        Mono<Connection> connectionMono = Mono.from(connectionFactory.create());;
        return connectionMono;
    }

    private Flux<Result> runSQLQuery(SQLQuery sqlQuery) {
        Flux<Result> flux = createConnection()
            .map(connection -> prepareStatement(connection, sqlQuery))
            .flatMapMany(statement -> statement.execute());

        return flux.doOnError(throwable -> log.info("SQL query execution error: query='{}'; error=",
            sqlQuery.getSqlExpression(), throwable))
            .doOnComplete(() -> log.info("SQL query was successfully executed: '{}'", sqlQuery.getSqlExpression()))
            .timeout(Duration.ofSeconds(1));
    }

    private Statement prepareStatement(Connection connection, SQLQuery sqlQuery) {
        Statement statement = connection.createStatement(sqlQuery.getSqlExpression());

        for (Map.Entry<String, Object> entry : sqlQuery.getParameters().entrySet()) {
            statement.bind(entry.getKey(), entry.getValue());
        }

        return statement;
    }

    private <T> Flux<T> createEntityFrom(Class<T> klass, Result result) {
        return Flux.from(result.map((row, rowMetaData) ->
                convertDBEntity(klass, row)))
            .flatMap(mono -> mono);
    }

    private <T> Mono<T> convertDBEntity(Class<T> klass, Row row) {
        Mono<T> monoEntity;

        try {
            Method staticConstructor = map.get(klass);
            if (staticConstructor == null) {
                throw new IllegalArgumentException(format("Class '%s' not known by RDBCAdapterImpl", klass));
            }

            Object entity = staticConstructor.invoke(null, row);
            monoEntity = Mono.just(klass.cast(entity));
        } catch (Exception e) {
            log.error("Error while converting DB entity to class '%s': ", klass, e);
            monoEntity = Mono.error(e);
        }

        return monoEntity;
    }
}
