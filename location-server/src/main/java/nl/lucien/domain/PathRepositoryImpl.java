package nl.lucien.domain;

import io.r2dbc.spi.Row;
import lombok.extern.slf4j.Slf4j;
import nl.lucien.adapter.LocationDto;
import nl.lucien.adapter.PathQuery;
import nl.lucien.configuration.RdbcAdapter;
import nl.lucien.configuration.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
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

    private static final String FIND_LOCATIONS_BY_PATHID = "select longitude, latitude, \"timestamp\"\n" +
        "from public.\"location\" \n" +
        "where pathid = $1 %s\n" +
        "order by \"timestamp\"";

    private static final String CREATE_NEW_PATH = "insert into public.path values ($1, $2)";
    private static final String CREATE_NEW_LOCATION = "insert into public.location values ($1, $2, $3, $4)";

    private RdbcAdapter rdbcAdapter;

    @Autowired
    public PathRepositoryImpl(RdbcAdapter rdbcAdapter) {
        this.rdbcAdapter = rdbcAdapter;
    }

    @Override
    public Flux<Path> queryPaths(PathQuery pathQuery) {
        return rdbcAdapter.findAll(SQLQuery.from(FIND_PATH_METADATA_BY_USERID, pathQuery.getUserId()), PathMetaData.class)
            .flatMap(pathMetaData -> buildPath(pathMetaData, pathQuery));
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
                SQLQuery sqlQuery = SQLQuery.from(CREATE_NEW_LOCATION, pathId,
                    locationDto.getLongitude(), locationDto.getLatitude(), locationDto.getTimestamp());
                return rdbcAdapter.insert(sqlQuery)
                    .filter(inserted -> inserted)
                    .flatMap(inserted -> buildPath(pathMetaData, null));
            });
    }

    private Mono<Path> buildPath(PathMetaData pathMetaData, PathQuery pathQuery) {
        return findLocationsByPathId(pathMetaData.getId(), pathQuery)
            .collectList()
            .map(locations -> Path.from(pathMetaData, locations));
    }

    private Mono<PathMetaData> findPathMetaDataByPathId(String pathId) {
        return rdbcAdapter.find(SQLQuery.from(FIND_PATH_METADATA_BY_PATHID, pathId), PathMetaData.class);
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
        String sqlBlock = FIND_LOCATIONS_BY_PATHID;

        if (pathQuery.getStartTime() != null) {
            sqlBlock += " and \"timestamp\" >= $2 ";
        }

        if (pathQuery.getEndTime() != null) {
            String identifier = (pathQuery.getStartTime() == null) ? "$2" : "$3";
            sqlBlock += " and \"timestamp\" <= " + identifier + " ";
        }

        String query = format(sqlBlock, sqlBlock);
        return query;
    }



}
