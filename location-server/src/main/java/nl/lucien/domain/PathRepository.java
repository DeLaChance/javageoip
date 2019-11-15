package nl.lucien.domain;

import nl.lucien.adapter.LocationDto;
import nl.lucien.adapter.PathQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PathRepository {

    Flux<Path> findAll();
    Mono<Path> findById(String pathId);
    Flux<Path> queryPaths(PathQuery pathQuery);
    Mono<Path> createPathFor(String userId);
    Mono<Path> addLocationToPath(String pathId, LocationDto locationDto);
    Mono<Path> deleteByPathId(String pathId);
}
