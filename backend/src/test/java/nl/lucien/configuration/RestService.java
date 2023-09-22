package nl.lucien.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import nl.lucien.geoip.adapter.GeoIpResponse;

@Service
public class RestService {

	@Value("${server.port}")
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

    @SneakyThrows
    public ResponseEntity<GeoIpResponse> findCountryByIpAddress(String ipAddress) {
        String url = determineBaseUrl() + "geoip/" + ipAddress + "/country";
        ResponseEntity<GeoIpResponse> responseEntity = restTemplate.getForEntity(url, GeoIpResponse.class);
        return responseEntity;
    }   

    private String determineBaseUrl() {
        return "http://localhost:" + port + "/api/";
    }
}