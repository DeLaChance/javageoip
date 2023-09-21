package nl.lucien.geoip.adapter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/geoip")
public class GeoIpController {

    @GetMapping("/{ipAddress}/country")
    public ResponseEntity<String> findCountryByIpAddress(@PathVariable("ipAddress") String ipAddress) {
        return new ResponseEntity<String>(ipAddress, HttpStatus.OK);
    }
    
}
