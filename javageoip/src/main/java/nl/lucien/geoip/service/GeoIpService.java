package nl.lucien.geoip.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import inet.ipaddr.IPAddressString;
import inet.ipaddr.ipv4.IPv4Address;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import nl.lucien.geoip.adapter.GeoIpResponse;
import nl.lucien.geoip.domain.Country;
import nl.lucien.geoip.domain.CountryRepository;
import reactor.core.publisher.Mono;

@Service
public class GeoIpService {
    
    private CountryRepository repository;

    @Autowired
    public GeoIpService(CountryRepository repository) {
        this.repository = repository;
    }

   public Mono<GeoIpResponse> findCountryByIpAddress(String ipAddress) {
        IPv4Address iPv4Address = new IPAddressString(ipAddress).getAddress().toIPv4();        
        Mono<Country> monoCountry = repository.findCountryByIpAddressOrError(iPv4Address.longValue());
        return monoCountry.map(country -> GeoIpResponse.from(country, iPv4Address));
    }
}
