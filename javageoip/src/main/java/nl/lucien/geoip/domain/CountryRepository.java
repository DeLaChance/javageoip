package nl.lucien.geoip.domain;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

public interface CountryRepository extends ReactiveCrudRepository<Country, Long> {

    String FIND_COUNTRY_BY_IP_ADDRESS = """
        select c.code, c.full_name 
        from geoip.countries c 
        join geoip.geo_ip_ranges gir on gir.countrycode = c.code 
        where gir.beginip <= $1 and gir.endip >= $1
    """;

    @Query(FIND_COUNTRY_BY_IP_ADDRESS)
    Mono<Country> findCountryByIpAddressOrError(long ipAddress);

}
