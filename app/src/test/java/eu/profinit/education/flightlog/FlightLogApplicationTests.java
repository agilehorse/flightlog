package eu.profinit.education.flightlog;

import eu.profinit.education.flightlog.domain.repositories.FlightRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = IntegrationTestConfig.class)
@Transactional
@Slf4j
@Tag("slow")
@Tag("integration")
@ActiveProfiles("stub")
public class FlightLogApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void flightsInAirAndLanding() throws Exception {
        ResponseEntity<List<Map>> flightsResponse = restTemplate.exchange("/flight/inAir", HttpMethod.GET, null, new ParameterizedTypeReference<List<Map>>(){});
        List<Map> flightsBefore = flightsResponse.getBody();

        int initialFlightsCount = flightsBefore.size();
        assertTrue(initialFlightsCount >= 1, "There should be at least one flight");
        assertEquals(5, flightsBefore.get(0).get("id"));

        String inputJson = readFileToString("landInput.json");
        HttpEntity<String> request = createRequestEntityWithHeaders(inputJson);
        ResponseEntity<Object> response = restTemplate.exchange("/flight/land", HttpMethod.POST, request, Object.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<List<Map>> flightsResponse2 = restTemplate.exchange("/flight/inAir", HttpMethod.GET, null, new ParameterizedTypeReference<List<Map>>(){});
        List<Map> flightsAfter = flightsResponse2.getBody();

        assertEquals(initialFlightsCount - 1, flightsAfter.size(), "There should one flight less than at the beginning");
    }

    private HttpEntity<String> createRequestEntityWithHeaders(String inputJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(inputJson, headers);
    }

    private String readFileToString(String fileName) throws IOException, URISyntaxException {
        return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(fileName).toURI())));
    }


}
