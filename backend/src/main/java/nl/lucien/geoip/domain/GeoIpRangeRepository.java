package nl.lucien.geoip.domain;

import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public class GeoIpRangeRepository {

    public Mono<Country> findCountryByIpAddressOrError(long longValue) {
        return Mono.just(Country.builder()
            .code("US")
            .name("United States of America")
            .build());
    }

}
