package nl.lucien.configuration.postgresql;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RdbcAdapter {

    <T> Flux<T> findAll(SQLQuery sqlQuery, Class<T> klass);
    <T> Mono<T> find(SQLQuery sqlQuery, Class<T> klass);

    Mono<Boolean> insert(SQLQuery sqlQuery);
    Mono<Boolean> update(SQLQuery sqlQuery);
    Mono<Boolean> delete(SQLQuery sqlQuery);
}
