package nl.lucien.domain;

import nl.lucien.adapter.PathQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PathRepository {

    Flux<Path> queryPaths(PathQuery pathQuery);
    Mono<Path> findById(String pathId);
}
