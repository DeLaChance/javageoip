package nl.lucien.geoip.domain;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder
public class GeoIp {

    private String ipAddress;    
}
