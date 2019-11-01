package nl.lucien.configuration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RdbcAdapter {

    <T> Flux<T> findAll(SQLQuery sqlQuery, Class<T> klass);
    <T> Mono<T> find(SQLQuery sqlQuery, Class<T> klass);

    <T> Mono<T> insert(SQLQuery sqlQuery, Class<T> klass);
    <T> Mono<T> update(SQLQuery sqlQuery, Class<T> klass);
    <T> Mono<T> delete(SQLQuery sqlQuery, Class<T> klass);
}
