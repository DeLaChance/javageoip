package nl.lucien.geoip.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.SneakyThrows;
import nl.lucien.Application;
import nl.lucien.configuration.RestService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
public class GeoIpControllerIntegrationTest {

    @Autowired
    private RestService restService;

    @SneakyThrows
    @DisplayName("Test that a country can be found by its IP address")
    @Test
    public void test_that_country_can_be_found_by_address() {
        // Given
        String ipAddress = "8.8.8.8";

        // When
        ResponseEntity<GeoIpResponse> response = restService.findCountryByIpAddress(ipAddress);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        GeoIpResponse payload = response.getBody();
        assertNotNull(payload);

        assertNotNull(payload.getCountry());
        assertEquals("US", payload.getCountry().getCode());
        assertEquals("United States of America", payload.getCountry().getName());

        assertNotNull(payload.getQuery());
        assertEquals(ipAddress, payload.getQuery().getIpAddress());
    }    
}