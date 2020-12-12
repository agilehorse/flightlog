package eu.profinit.education.flightlog;

import eu.profinit.education.flightlog.exceptions.ExternalSystemException;
import eu.profinit.education.flightlog.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = IntegrationTestConfig.class)
@Transactional
@Slf4j
@Tag("slow")
@Tag("unit")
public class UserControllerUnitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private PersonService personService;

    @Test
    public void getClubMembersReturnsServiceUnavailableIfRemoteDBIsNotAvailable() {
        given(personService.getClubMembers()).willThrow(ExternalSystemException.class);

        ResponseEntity<String> flightsResponse = restTemplate.exchange("/user", HttpMethod.GET, null, new ParameterizedTypeReference<String>(){});

        Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE, flightsResponse.getStatusCode());
    }

    @Test
    public void getClubMembersGetsCalledCorrectly() {
        Mockito.doReturn(null).when(personService).getClubMembers();

        restTemplate.exchange("/user", HttpMethod.GET, null, new ParameterizedTypeReference<String>(){});

        Mockito.verify(personService, times(1)).getClubMembers();
    }
}
