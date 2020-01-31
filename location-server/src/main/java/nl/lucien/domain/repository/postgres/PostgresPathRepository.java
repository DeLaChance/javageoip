package nl.lucien.domain.repository.postgres;

import lombok.extern.slf4j.Slf4j;
import nl.lucien.adapter.LocationDto;
import nl.lucien.adapter.PathQuery;
import nl.lucien.configuration.postgresql.RdbcAdapter;
import nl.lucien.configuration.postgresql.SQLQuery;
import nl.lucien.domain.Location;
import nl.lucien.domain.Path;
import nl.lucien.domain.PathMetaData;
import nl.lucien.domain.PathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

import static java.lang.String.format;

@Repository
@Slf4j
public class PostgresPathRepository implements PathRepository {

    private static final String FIND_ALL_PATH_METADATA = "select p.id, p.userid, count(*)-1 as length\n" +
        "from locationcloud.\"path\" as p\n" +
        "left join locationcloud.\"location\" as l on l.pathid = p.id\n" +
        "group by p.id, p.userid";

    private static final String FIND_PATH_METADATA_BY_PATHID = "select p.id, p.userid, count(*)-1 as length\n" +
        "from locationcloud.\"path\" as p\n" +
        "left join locationcloud.\"location\" as l on l.pathid = p.id\n" +
        "where p.id = $1\n" +
        "group by p.id, p.userid";

    private static final String FIND_PATH_METADATA_BY_USERID = "select p.id, p.userid, count(*)-1 as length\n" +
        "from locationcloud.\"path\" as p\n" +
        "left join locationcloud.\"location\" as l on l.pathid = p.id\n" +
        "where p.userid = $1\n" +
        "group by p.id, p.userid";

    private static final String FIND_LOCATIONS_BY_PATHID = "select l.\"longitude\", l.\"latitude\", l.\"timestamp\"\n" +
        "from locationcloud.\"location\" as l\n" +
        "where l.\"pathid\" = $1 %s\n" +
        "order by l.\"timestamp\"";

    private static final String CREATE_NEW_PATH = "insert into locationcloud.path values ($1, $2)";
    private static final String CREATE_NEW_LOCATION = "insert into locationcloud.location values ($1, $2, $3, $4, $5)";

    private static final String DELETE_PATH_BY_ID = "delete from locationcloud.location where id = $1";

    private RdbcAdapter rdbcAdapter;

    @Autowired
    public PostgresPathRepository(RdbcAdapter rdbcAdapter) {
        this.rdbcAdapter = rdbcAdapter;
    }

    @Override
    public Flux<Path> queryPaths(PathQuery pathQuery) {
        return rdbcAdapter.findAll(SQLQuery.from(FIND_PATH_METADATA_BY_USERID, pathQuery.getUserId()), PathMetaData.class)
            .flatMap(pathMetaData -> buildPath(pathMetaData, pathQuery));
    }

    @Override
    public Flux<Path> findAll() {
        return rdbcAdapter.findAll(SQLQuery.from(FIND_ALL_PATH_METADATA), PathMetaData.class)
            .flatMap(pathMetaData -> buildPath(pathMetaData, null));
    }

    @Override
    public Mono<Path> findById(String pathId) {
        return rdbcAdapter.find(SQLQuery.from(FIND_PATH_METADATA_BY_PATHID, pathId), PathMetaData.class)
            .flatMap(pathMetaData -> buildPath(pathMetaData, null));
    }

    @Override
    public Mono<Path> createPathFor(String userId) {
        String pathId = UUID.randomUUID().toString();
        return rdbcAdapter.insert(SQLQuery.from(CREATE_NEW_PATH, pathId, userId))
            .filter(inserted -> inserted)
            .flatMap(inserted -> findPathMetaDataByPathId(pathId)
                .map(pathMetaData -> Path.from(pathMetaData, Arrays.asList()))
            );
    }

    @Override
    public Mono<Path> addLocationToPath(String pathId, LocationDto locationDto) {
        return findPathMetaDataByPathId(pathId)
            .flatMap(pathMetaData -> {
                String locationId = UUID.randomUUID().toString();
                SQLQuery sqlQuery = SQLQuery.from(CREATE_NEW_LOCATION, locationId, pathId, locationDto.getTimestamp(),
                    locationDto.getLatitude(), locationDto.getLongitude());
                return rdbcAdapter.insert(sqlQuery)
                    .filter(inserted -> inserted)
                    .flatMap(inserted -> buildPath(pathMetaData, null));
            });
    }

    @Override
    public Mono<Path> deleteByPathId(String pathId) {
        return findPathMetaDataByPathId(pathId)
            .flatMap(path -> rdbcAdapter.delete(SQLQuery.from(DELETE_PATH_BY_ID, pathId))
                .doOnError(throwable -> log.error("Cannot delete '{}' due to exception: ", pathId, throwable))
                .filter(deleted -> deleted)
                .map(deleted -> path)
            )
            .map(pathMetaData -> Path.from(pathMetaData, Collections.EMPTY_LIST));
    }

    private Mono<Path> buildPath(PathMetaData pathMetaData, PathQuery pathQuery) {
        return findLocationsByPathId(pathMetaData.getId(), pathQuery)
            .collectList()
            .map(locations -> Path.from(pathMetaData, locations));
    }

    private Mono<PathMetaData> findPathMetaDataByPathId(String pathId) {
        SQLQuery sqlQuery = SQLQuery.from(FIND_PATH_METADATA_BY_PATHID, pathId);
        return rdbcAdapter.find(sqlQuery, PathMetaData.class);
    }

    private Flux<Location> findLocationsByPathId(String pathId, PathQuery pathQuery) {
        return rdbcAdapter.findAll(buildLocationsQuery(pathId, pathQuery), Location.class);
    }

    private SQLQuery buildLocationsQuery(String pathId, PathQuery pathQuery) {

        List<Object> args = new ArrayList<>();
        args.add(pathId);

        String queryString = buildQueryLocationSqlQuery(pathQuery);
        if (pathQuery != null) {
            if (pathQuery.getStartTime() != null) {
                args.add(pathQuery.getStartTime());
            }

            if (pathQuery.getEndTime() != null) {
                args.add(pathQuery.getEndTime());
            }
        }

        SQLQuery sqlQuery = SQLQuery.from(queryString, args);
        return sqlQuery;
    }

    private String buildQueryLocationSqlQuery(PathQuery pathQuery) {
        String sqlBlock = "";

        if (pathQuery != null) {

            if (pathQuery.getStartTime() != null) {
                sqlBlock += " and \"timestamp\" >= $2 ";
            }

            if (pathQuery.getEndTime() != null) {
                String identifier = (pathQuery.getStartTime() == null) ? "$2" : "$3";
                sqlBlock += " and \"timestamp\" <= " + identifier + " ";
            }
        }

        String query = format(FIND_LOCATIONS_BY_PATHID, sqlBlock);
        return query;
    }



}
