package nl.lucien.geoip.adapter;

import inet.ipaddr.ipv4.IPv4Address;
import lombok.Builder;
import lombok.Data;
import nl.lucien.geoip.domain.Country;

@Data
@Builder
public class GeoIpResponse {

    private CountryDto country;
    private QueryDto query;

    public static GeoIpResponse from(Country country, IPv4Address ipAddress) {
        return GeoIpResponse.builder()
            .country(CountryDto.from(country))
            .query(new QueryDto(ipAddress.toFullString(), ipAddress.longValue()))
            .build();
    } 
 
    public static record CountryDto(String code, String fullName) {        

        public static CountryDto from(Country country) {
            return new CountryDto(country.getCode(), country.getName());
        }
    }    
    
    public static record QueryDto(String ipAddress, Long ipAddressNumeric) {        
    }
}
