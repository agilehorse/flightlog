package eu.profinit.education.flightlog.service;

import eu.profinit.education.flightlog.domain.codebooks.AirplaneType;
import eu.profinit.education.flightlog.domain.codebooks.ClubAirplane;
import eu.profinit.education.flightlog.domain.repositories.ClubAirplaneRepository;
import eu.profinit.education.flightlog.to.AirplaneTo;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag("fast")
@Tag("unit")
public class AirplaneServiceTest {

    @Mock
    private ClubAirplaneRepository clubAirplaneRepository;

    @InjectMocks
    private AirplaneServiceImpl testSubject;

    private List<ClubAirplane> clubAirplanes;
    private List<AirplaneTo> airplaneTosExpected;

    @BeforeEach
    void setUp() {
        // init mocks
        MockitoAnnotations.initMocks(this);
        // club airplanes
        clubAirplanes = new ArrayList<>();
        ClubAirplane clubAirplane1 = new ClubAirplane(1L, "OK-V23424", new AirplaneType(1L, "WT-9 Dynamic", 2), false);
        ClubAirplane clubAirplane2 = new ClubAirplane(2L, "OK-B123", new AirplaneType(2L, "L-13A Blaník", 1), false);
        ClubAirplane clubAirplane3 = new ClubAirplane(3L, "OK-D123", new AirplaneType(3L, "Zlín Z-42M", 1), false);

        clubAirplanes.add(clubAirplane2);
        clubAirplanes.add(clubAirplane3);
        clubAirplanes.add(clubAirplane1);

        // expected airplanes
        airplaneTosExpected = new ArrayList<>();

        AirplaneTo airplaneTo1 = new AirplaneTo(1L, "OK-V23424", "WT-9 Dynamic");
        AirplaneTo airplaneTo2 = new AirplaneTo(2L, "OK-B123", "L-13A Blaník");
        AirplaneTo airplaneTo3 = new AirplaneTo(3L, "OK-D123", "Zlín Z-42M");

        airplaneTosExpected.add(airplaneTo2);
        airplaneTosExpected.add(airplaneTo3);
        airplaneTosExpected.add(airplaneTo1);
    }

    @Test
    public void getClubAirplanesShouldReturnCorrectAirplanes() {
        // mock the repository
        Mockito.doReturn(clubAirplanes).when(clubAirplaneRepository).findAll(Mockito.any(Sort.class));

        // assert that arrays equal
        assertThat(testSubject.getClubAirplanes(), Matchers.hasItems(
            airplaneTosExpected.get(0),
            airplaneTosExpected.get(1),
            airplaneTosExpected.get(2)
        ));
    }

    @Test
    public void getClubAirplanesShouldThrowValidationException() {
        ClubAirplane clubAirplaneArchived = new ClubAirplane(4L, "OK-C", new AirplaneType(4L, "ASW 15 B", 1), true);
        clubAirplanes.add(clubAirplaneArchived);

        // mock the repository
        Mockito.doReturn(clubAirplanes).when(clubAirplaneRepository).findAll(Mockito.any(Sort.class));

        Exception exception = assertThrows(ConstraintViolationException.class, () -> testSubject.getClubAirplanes());
    }

    @Test
    public void getClubAirplanesShouldReturnAirplanesPreservesOrder() {
        // mock the repository
        Mockito.doReturn(clubAirplanes).when(clubAirplaneRepository).findAll(Mockito.any(Sort.class));

        List<AirplaneTo> actualClubAirplanes = testSubject.getClubAirplanes();

        // assert that the returned list is ordered as expected
        for (int i = 0; i < airplaneTosExpected.size(); i++) {
            assertThat(actualClubAirplanes.get(i), is(airplaneTosExpected.get(i)));
        }
    }

    @Test
    @Disabled // enable later, this one will not pass because of missing validation
    public void getClubAirplanesShouldReturnOnlyAirplanesThatAreNotArchived() {
        ClubAirplane clubAirplaneArchived = new ClubAirplane(4L, "OK-C222", new AirplaneType(4L, "ASW 15 B", 1), true);
        clubAirplanes.add(clubAirplaneArchived);

        AirplaneTo airplaneTo4 = new AirplaneTo(4L, "OK-C222", "ASW 15 B");

        // mock the repository
        Mockito.doReturn(clubAirplanes).when(clubAirplaneRepository).findAll(Mockito.any(Sort.class));

        assertFalse(testSubject.getClubAirplanes().contains(airplaneTo4), "Actual returned airplane list should not contain archived club airplanes.");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/csv/airplanes/data.csv")
    public void getAllClubAirplanesShouldRaiseExceptionForInvalidRecords(String imatriculation, int capacity, String type, boolean valid) {
        if (imatriculation.equals("nill"))
            imatriculation = null;

        if (type.equals("nill"))
            type = null;

        List<ClubAirplane> mockedClubAirplanes = new ArrayList<>();
        ClubAirplane clubAirplane1 = new ClubAirplane(1L, imatriculation, new AirplaneType(1L, type, capacity), false);
        mockedClubAirplanes.add(clubAirplane1);

        AirplaneTo expectedAirplane = new AirplaneTo(1L, imatriculation, type);

        // mock the repository
        Mockito.doReturn(mockedClubAirplanes).when(clubAirplaneRepository).findAll(Mockito.any(Sort.class));

        if (valid) {
            assertTrue(testSubject.getClubAirplanes().contains(expectedAirplane));
        } else {
            Exception exception = assertThrows(ConstraintViolationException.class, () -> testSubject.getClubAirplanes());
            assertTrue(exception.getMessage().contains("Validation exceptions raised when accessing airplanes from database. Please contact the administrator."));
        }
    }

}