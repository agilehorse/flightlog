package eu.profinit.education.flightlog;

import eu.profinit.education.flightlog.service.AirplaneService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = IntegrationTestConfig.class)
@Transactional
@Slf4j
@Tag("slow")
@Tag("unit")
public class AirplaneControllerUnitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private AirplaneService airplaneService;

    @Test
    public void getClubAirplanesReturnsInternalServerErrorIfThereIsAnErrorWithDataFromDatabase() {
        given(airplaneService.getClubAirplanes()).willThrow(ConstraintViolationException.class);

        ResponseEntity<String> flightsResponse = restTemplate.exchange("/airplane", HttpMethod.GET, null, new ParameterizedTypeReference<String>(){});

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, flightsResponse.getStatusCode());
    }


}
