package nl.lucien.geoip.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import inet.ipaddr.IPAddressString;
import inet.ipaddr.ipv4.IPv4Address;
import nl.lucien.geoip.adapter.GeoIpResponse;
import nl.lucien.geoip.domain.Country;
import nl.lucien.geoip.domain.GeoIpRangeRepository;
import reactor.core.publisher.Mono;

@Service
public class GeoIpService {
    
    private GeoIpRangeRepository repository;

    @Autowired
    public GeoIpService(GeoIpRangeRepository repository) {
        this.repository = repository;
    }

    public Mono<GeoIpResponse> findCountryByIpAddress(String ipAddress) {
        IPv4Address iPv4Address = new IPAddressString(ipAddress).getAddress().toIPv4();        
        Mono<Country> monoCountry = repository.findCountryByIpAddressOrError(iPv4Address.longValue());
        return monoCountry.map(country -> GeoIpResponse.from(country, iPv4Address));
    }
}
