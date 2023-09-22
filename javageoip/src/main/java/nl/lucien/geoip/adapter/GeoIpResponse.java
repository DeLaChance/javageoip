package nl.lucien.geoip.adapter;

import inet.ipaddr.ipv4.IPv4Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import nl.lucien.geoip.domain.Country;

@Data
@Builder
public class GeoIpResponse {

    private CountryDto country;
    private QueryDto query;

    public static GeoIpResponse from(Country country, IPv4Address ipAddress) {
        return GeoIpResponse.builder()
            .country(CountryDto.from(country))
            .query(new QueryDto(ipAddress.toString(), ipAddress.longValue()))
            .build();
    } 
 
    @Getter
    @AllArgsConstructor
    public static class CountryDto {        

        private String code;
        private String name;

        public static CountryDto from(Country country) {
            return new CountryDto(country.getCode(), country.getName());
        }
    }    
    
    @Getter
    @AllArgsConstructor
    public static class QueryDto {        
        private String ipAddress;
        private Long ipAddressNumeric;
    }
}
