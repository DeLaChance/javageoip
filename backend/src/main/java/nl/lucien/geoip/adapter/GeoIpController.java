package nl.lucien.geoip.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.lucien.geoip.service.GeoIpService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/geoip")
public class GeoIpController {

    private GeoIpService service;
    
    @Autowired
    public GeoIpController(GeoIpService service) {
        this.service = service;
    }

    @GetMapping("/{ipAddress}/country")
    public Mono<GeoIpResponse> findCountryByIpAddress(@PathVariable("ipAddress") String ipAddress) {
        return service.findCountryByIpAddress(ipAddress);
    }
    
}
