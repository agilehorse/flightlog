package eu.profinit.education.flightlog.domain.repositories;

import eu.profinit.education.flightlog.IntegrationTestConfig;
import eu.profinit.education.flightlog.domain.entities.Flight;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = IntegrationTestConfig.class)
@Transactional
@TestPropertySource(
    locations = "classpath:application-integrationtest.properties")
public class FlightRepositoryTest {

    @Autowired
    private FlightRepository testSubject;

    @Test
    public void shouldLoadFlights() {

        List<Flight> flights = testSubject.findAll();

        assertEquals(5, flights.size(), "There should be 5 flights");

    }

    @Test
    public void shouldLoadGliderFlights() {
        List<Flight> flights = testSubject.findAllByFlightType(Flight.Type.GLIDER);

        assertEquals(2, flights.size(), "There should be 2 glider flights");
    }

    @Test
    public void shouldLoadFlightsInTheAir() {
        List<Flight> flights = testSubject.findAllByLandingTimeNullOrderByTakeoffTimeAscIdAsc();

        assertEquals(3, flights.size(), "There should be 3 flights");
        assertEquals(5L, flights.get(0).getId().getId().longValue(), "Flight with ID 5 started first and should be first");
        assertEquals(1L, flights.get(1).getId().getId().longValue(), "Flight with ID 1 should be second");
        assertEquals(2L, flights.get(2).getId().getId().longValue(), "Flight with ID 2 should be third");
    }
}