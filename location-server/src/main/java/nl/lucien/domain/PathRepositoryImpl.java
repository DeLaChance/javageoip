package nl.lucien.domain;

import io.r2dbc.postgresql.PostgresqlConnection;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.PostgresqlStatement;
import io.r2dbc.spi.Row;
import lombok.extern.slf4j.Slf4j;
import nl.lucien.adapter.PathQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Repository
@Slf4j
public class PathRepositoryImpl implements PathRepository {

    private static final String FIND_PATH_METADATA_BY_PATHID = "select p.id, p.userid, count(*) as length\n" +
        "from public.\"path\" as p\n" +
        "inner join public.\"location\" as l on l.pathid = p.id\n" +
        "where p.id = $1\n" +
        "group by p.id, p.userid";

    private static final String FIND_PATH_METADATA_BY_USERID = "select p.id, p.userid, count(*) as length\n" +
        "from public.\"path\" as p\n" +
        "inner join public.\"location\" as l on l.pathid = p.id\n" +
        "where p.userid = $1\n" +
        "group by p.id, p.userid";

    private static final String FIND_PATH_BY_ID = "select longitude, latitude, \"timestamp\"\n" +
        "from public.\"location\" \n" +
        "where pathid = $1\n" +
        "order by \"timestamp\"";

    private static final String QUERY_LOCATIONS = "select longitude, latitude, \"timestamp\"\n" +
        "from public.\"location\" \n" +
        "where pathid = $1\n" +
        "%s \n" +
        "order by \"timestamp\"";

    // TODO: replace with interface instead of implementation
    private PostgresqlConnectionFactory connectionFactory;

    @Autowired
    public PathRepositoryImpl(PostgresqlConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Flux<Path> queryPaths(PathQuery pathQuery) {
        return findAllPathMetaDataOfUserWith(pathQuery.getUserId())
            .flatMap(pathMetaData -> queryLocationsBy(pathMetaData.getId(), pathQuery)
                .flatMap(locations -> buildPathFrom(locations, pathMetaData.getId()))
            );
    }

    @Override
    public Mono<Path> findById(String pathId) {
        return queryLocationsBy(pathId, null)
            .flatMap(locations -> buildPathFrom(locations, pathId))
            .doOnError(throwable -> log.error("Could not find path with id '{}' due to: ", pathId, throwable));
    }

    @Override
    public Mono<Path> createPathFor(String userId) {
        String pathId = UUID.randomUUID().toString();
        return connectionFactory.create().flatMapMany(connetion ->
            connetion.createStatement("insert into public.path values ($1, $2)")
                .bind("$1", pathId)
                .bind("$2", userId)
                .execute()
        )
        .doOnError(throwable -> log.error("Could not construct path for user'{}' due to: ", userId, throwable))
        .next()
        .flatMap(result -> result.getRowsUpdated()
            .map(noOfRowsUpdated -> noOfRowsUpdated == 1)
        ).flatMap(created -> {
            Mono<Path> monoPath;
            if (created) {
                monoPath = findPathMetaDataByPathId(pathId)
                    .map(pathMetaData -> Path.from(pathMetaData, Arrays.asList()));
            } else {
                monoPath = Mono.empty();
            }

            return monoPath;
        });
    }

    private Flux<PathMetaData> findAllPathMetaDataOfUserWith(String userId) {
        return connectionFactory.create().flatMapMany(connection ->
            connection.createStatement(FIND_PATH_METADATA_BY_USERID)
                .bind("$1", userId)
                .execute()
        )
        .flatMap(result -> result.map((row, rowMetaData) -> PathMetaData.from(row)));
    }

    private Mono<List<Location>> queryLocationsBy(String pathId, PathQuery pathQuery) {
        return connectionFactory.create().flatMapMany(connection ->
            buildStatement(connection, pathId, pathQuery)
            .execute()
        )
        .flatMap(result -> result.map((row, rowMetaData) -> locationFromRow(row)))
        .collectList();
    }

    private PostgresqlStatement buildStatement(PostgresqlConnection connection, String pathId, PathQuery pathQuery) {
        PostgresqlStatement statement;

        if (pathQuery == null) {
            statement = connection.createStatement(FIND_PATH_BY_ID)
                .bind("$1", pathId);
        } else {
            statement = connection.createStatement(buildQueryLocationSqlQuery(pathQuery))
                .bind("$1", pathId);

            if (pathQuery.getStartTime() != null) {
                statement = statement.bind("$2", pathQuery.getStartTime());
            }

            if (pathQuery.getEndTime() != null) {
                String identifier = (pathQuery.getStartTime() == null) ? "$2" : "$3";
                statement = statement.bind(identifier, pathQuery.getEndTime());
            }
        }

        return statement;
    }

    private String buildQueryLocationSqlQuery(PathQuery pathQuery) {
        String sqlBlock = "";

        if (pathQuery.getStartTime() != null) {
            sqlBlock += " and \"timestamp\" >= $2 ";
        }

        if (pathQuery.getEndTime() != null) {
            String identifier = (pathQuery.getStartTime() == null) ? "$2" : "$3";
            sqlBlock += " and \"timestamp\" <= " + identifier + " ";
        }

        String query = format(QUERY_LOCATIONS, sqlBlock);
        return query;
    }

    private Location locationFromRow(Row row) {
        Double longitude = row.get("longitude", Double.class);
        Double latitude = row.get("latitude", Double.class);
        Long unixTimestamp = row.get("timestamp", Long.class);

        Location location = Location.builder()
                .latitude(latitude)
                .longitude(longitude)
                .timestamp(unixTimestamp)
                .build();

        return location;
    }

    private Mono<Path> buildPathFrom(List<Location> locations, String pathId) {
        return findPathMetaDataByPathId(pathId)
            .map(pathMetaData -> Path.from(pathMetaData, locations));
    }

    private Mono<PathMetaData> findPathMetaDataByPathId(String pathId) {
        return connectionFactory.create().flatMapMany(connection ->
            connection.createStatement(FIND_PATH_METADATA_BY_PATHID)
                .bind("$1", pathId)
                .execute()
        )
        .next()
        .flatMap(result -> result.map((row, rowMetaData) -> PathMetaData.from(row)).next());
    }

}
