package eu.profinit.education.flightlog.service;

import eu.profinit.education.flightlog.common.ClockImpl;
import eu.profinit.education.flightlog.domain.entities.Airplane;
import eu.profinit.education.flightlog.domain.entities.Flight;
import eu.profinit.education.flightlog.domain.entities.FlightId;
import eu.profinit.education.flightlog.domain.fields.Task;
import eu.profinit.education.flightlog.domain.repositories.ClubAirplaneRepository;
import eu.profinit.education.flightlog.domain.repositories.FlightRepository;
import eu.profinit.education.flightlog.exceptions.NotFoundException;
import eu.profinit.education.flightlog.exceptions.ValidationException;
import eu.profinit.education.flightlog.service.datasource.PersonDataSource;
import eu.profinit.education.flightlog.to.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("fast")
@Tag("unit")
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private ClubAirplaneRepository clubAirplaneRepository;

    @Mock
    private PersonService personService;

    @Mock
    private ClockImpl clock;


    @InjectMocks
    private FlightServiceImpl testSubject;

    @BeforeEach
    public void setUp(){
        // init mocks
        MockitoAnnotations.initMocks(this);
        testSubject = new FlightServiceImpl(flightRepository, clubAirplaneRepository, clock, personService);
    }

    // TAKEOFF
    @Nested
    @DisplayName("Flight takeoff tests")
    class Takeoff {

        private FlightTakeoffTo generateTakeoff(LocalDateTime takeofftime, String task,
                                           AirplaneTo towplane,
                                           String note, AirplaneTo glider, String noteGlider) {

            return FlightTakeoffTo.builder()
                .takeoffTime(takeofftime)
                .task(task)
                .towplane(AirplaneWithCrewTo.builder()
                    .airplane(
                        towplane
                    )
                    .pilot(
                        PersonDataSource.clubPersonTo
                    )
                    .copilot(
                        PersonDataSource.guestPersonTo
                    )
                    .note(note)
                    .build()
                )
                .glider(AirplaneWithCrewTo.builder()
                    .airplane(
                        glider
                    )
                    .pilot(
                        PersonDataSource.guestPersonTo2
                    )
                    .copilot(
                        PersonDataSource.clubPersonTo2
                    )
                    .note(noteGlider)
                    .build()
                )
                .build();
        }

        @Test
        public void takeoffWithNullTakeoffTimeMustFailWithValidationException(){

            FlightTakeoffTo flightTakeoffTo = generateTakeoff(null, "VLEK",
                AirplaneTo.builder()
                    .type("L-13A Blaník")
                    .immatriculation("OK-V23424")
                    .build(),"Note for towplane",
                AirplaneTo.builder()
                    .type("ASW 15 B")
                    .immatriculation("OK-B123")
                    .build(), "Note for glider");

            Mockito.doReturn(null).when(flightRepository).save(Mockito.any());

            assertThrows(ValidationException.class, () ->
                testSubject.takeoff(flightTakeoffTo));
        }

        @Test
        @Disabled // TODO: fix the nullpointer exception if towplane is null
        public void takeoffWithNullTowplaneMustFailWithValidationException(){
            LocalDateTime takeoffTime = LocalDateTime.of(2020, 9, 8, 12, 20, 30);
            FlightTakeoffTo flightTakeoffTo = generateTakeoff(takeoffTime, "VLEK",
                null,"Note for towplane",
                AirplaneTo.builder()
                    .type("ASW 15 B")
                    .immatriculation("OK-B123")
                    .build(), "Note for glider");

            Mockito.doReturn(null).when(flightRepository).save(Mockito.any());

            assertThrows(ValidationException.class, () ->
                testSubject.takeoff(flightTakeoffTo));
        }

        @Test
        @Disabled // TODO: fix the nullpointer exception if glider is null
        public void takeoffWithNullGliderTakesoffCorrectly(){
            LocalDateTime takeoffTime = LocalDateTime.of(2020, 9, 8, 12, 20, 30);

            FlightTakeoffTo flightTakeoffTo = generateTakeoff(takeoffTime, "VLEK",
                AirplaneTo.builder()
                    .type("L-13A Blaník")
                    .immatriculation("OK-V23424")
                    .build(),"Note for towplane",
                null, null);


            Mockito.doReturn(null).when(flightRepository).save(Mockito.any());
            testSubject.takeoff(flightTakeoffTo);

            assertEquals(takeoffTime, Mockito.verify(flightRepository).save(Mockito.any(Flight.class)).getTakeoffTime());;
        }

        @Test
        public void takeoffCreatesFlightWithNullLandingTimeAndCorrectTakeoffTime() {
            LocalDateTime takeoffTime = LocalDateTime.of(2020, 9, 8, 12, 20, 30);

            FlightTakeoffTo flightTakeoffTo = generateTakeoff(takeoffTime, "VLEK",
                AirplaneTo.builder()
                    .type("L-13A Blaník")
                    .immatriculation("OK-V23424")
                    .build(),"Note for towplane",
                AirplaneTo.builder()
                    .type("ASW 15 B")
                    .immatriculation("OK-B123")
                    .build(), "Note for glider");

            Mockito.doReturn(null).when(flightRepository).save(Mockito.any());

            testSubject.takeoff(flightTakeoffTo);

            ArgumentCaptor<Flight> gliderArgument = ArgumentCaptor.forClass(Flight.class);
            ArgumentCaptor<Flight> towplaneArgument = ArgumentCaptor.forClass(Flight.class);

            Mockito.verify(flightRepository, times(2)).save(towplaneArgument.capture());
            Mockito.verify(flightRepository, times(2)).save(gliderArgument.capture());

            assertNull(towplaneArgument.getValue().getLandingTime());
            assertNull(gliderArgument.getValue().getLandingTime());
        }

        @Test
        public void takeOffCreatesFlightForBothGliderAndTowplane() {
            LocalDateTime takeoffTime = LocalDateTime.of(2020, 9, 8, 12, 20, 30);

            FlightTakeoffTo flightTakeoffTo = generateTakeoff(takeoffTime, "VLEK",
                AirplaneTo.builder()
                    .type("L-13A Blaník")
                    .immatriculation("OK-V23424")
                    .build(),"Note for towplane",
                AirplaneTo.builder()
                    .type("ASW 15 B")
                    .immatriculation("OK-B123")
                    .build(), "Note for glider");

            Mockito.doReturn(null).when(flightRepository).save(Mockito.any());

            testSubject.takeoff(flightTakeoffTo);

            ArgumentCaptor<Flight> gliderArgument = ArgumentCaptor.forClass(Flight.class);
            ArgumentCaptor<Flight> towplaneArgument = ArgumentCaptor.forClass(Flight.class);

            Mockito.verify(flightRepository, times(2)).save(towplaneArgument.capture());
            Mockito.verify(flightRepository, times(2)).save(gliderArgument.capture());

            assertEquals(towplaneArgument.getValue().getTakeoffTime(), takeoffTime);
            assertEquals(gliderArgument.getValue().getTakeoffTime(), takeoffTime);
        }
    }

    // LANDING
    @Nested
    @DisplayName("Flight landing tests")
    class Landing {

        private Flight generateFlight(Long id, Flight.Type type, LocalDateTime takeoff, LocalDateTime landing) {
            return new Flight(id, type, Task.TOWPLANE_TASK, takeoff, landing,
                new Airplane(null, "OK-B123", "ASW 15 B"),
                PersonDataSource.clubPerson, PersonDataSource.guestPerson,
                "Generic note", null, null);
        }

        @Test
        public void flightGetsEndedCorrectlyWithGivenLandingTime() {
            LocalDateTime takeoffTime = LocalDateTime.of(2020, 9, 8, 12, 20, 30);
            LocalDateTime landingTime = LocalDateTime.of(2020, 9, 8, 16, 40, 30);

            Flight flight = generateFlight(1L, Flight.Type.TOWPLANE, takeoffTime, null);

            // mock
            Mockito.doReturn(Optional.of(flight)).when(flightRepository).findById(Mockito.eq(1L));

            // act
            testSubject.land(FlightId.of(1L), landingTime);

            // verify save gets called and assert
            ArgumentCaptor<Flight> flightArgumentCaptor = ArgumentCaptor.forClass(Flight.class);
            Mockito.verify(flightRepository).save(flightArgumentCaptor.capture());
            assertEquals(flight.getLandingTime(), flightArgumentCaptor.getValue().getLandingTime());
        }

        @Test
        public void flightLandingCannotPreceedItsTakeoffAndShouldFailWithException() {
            LocalDateTime takeoffTime = LocalDateTime.of(2020, 9, 8, 18, 20, 30);
            LocalDateTime landingTime = LocalDateTime.of(2020, 9, 8, 16, 40, 30);

            Flight flight = generateFlight(1L, Flight.Type.TOWPLANE, takeoffTime, null);

            // mock
            Mockito.doReturn(Optional.of(flight)).when(flightRepository).findById(Mockito.eq(1L));

            assertThrows(ValidationException.class, () ->
                testSubject.land(FlightId.of(1L), landingTime));
        }

        @Test
        public void flightLandingIsSetToNowIfNoValueIsProvided() {
            LocalDateTime takeoffTime = LocalDateTime.of(2020, 9, 8, 12, 20, 30);
            LocalDateTime nowTime = LocalDateTime.of(2020, 9, 8, 16, 40, 30);

            Flight flight = generateFlight(1L, Flight.Type.TOWPLANE, takeoffTime, null);

            // mock
            Mockito.doReturn(Optional.of(flight)).when(flightRepository).findById(Mockito.eq(1L));
            Mockito.doReturn(nowTime).when(clock).now();

            // act
            testSubject.land(FlightId.of(1L), null);

            // verify save gets called and assert
            ArgumentCaptor<Flight> flightArgumentCaptor = ArgumentCaptor.forClass(Flight.class);
            Mockito.verify(flightRepository).save(flightArgumentCaptor.capture());
            assertEquals(flight.getLandingTime(), flightArgumentCaptor.getValue().getLandingTime());
        }

        @Test
        public void cannotLandAlreadyLandedFlight() {
            LocalDateTime takeoffTime = LocalDateTime.of(2020, 9, 8, 18, 20, 30);
            LocalDateTime landingTime = LocalDateTime.of(2020, 9, 8, 16, 40, 30);

            Flight flight = generateFlight(1L, Flight.Type.TOWPLANE, takeoffTime, landingTime);

            // mock
            Mockito.doReturn(Optional.of(flight)).when(flightRepository).findById(Mockito.eq(1L));

            assertThrows(ValidationException.class, () ->
                testSubject.land(FlightId.of(1L), landingTime));
        }

        @Test
        public void landingFailsForNullFlightId() {
            LocalDateTime landingTime = LocalDateTime.of(2020, 9, 8, 16, 40, 30);

            assertThrows(IllegalArgumentException.class, () ->
                testSubject.land(null, landingTime));
        }

        @Test
        public void landingReturnsNotFoundExceptionForNonExistingFlight() {
            LocalDateTime landingTime = LocalDateTime.of(2020, 9, 8, 16, 40, 30);

            // mock
            Mockito.doReturn(Optional.empty()).when(flightRepository).findById(Mockito.eq(1L));

            assertThrows(NotFoundException.class, () ->
                testSubject.land(FlightId.of(1L), landingTime));
        }
    }

    // ACTIVE FLIGHTS
    @Nested
    @DisplayName("Active flights screen tests")
    class ActiveFlightsScreen {

        private FlightTo generateFlightTo(Long id, String type, String imatriculation, LocalDateTime landing, LocalDateTime takeoff) {
            return FlightTo.builder()
                .id(id)
                .airplane(AirplaneTo.builder().type(type).immatriculation(imatriculation).build())
                .landingTime(landing)
                .takeoffTime(takeoff)
                .pilot(PersonDataSource.clubPersonTo2)
                .copilot(PersonDataSource.guestPersonTo)
                .task("VLEK")
                .build();
        }

        private Flight generateFlight(Long id, String type, String imatriculation, LocalDateTime landing, LocalDateTime takeoff) {
            return new Flight(id, Flight.Type.TOWPLANE, Task.TOWPLANE_TASK, takeoff, landing, new Airplane( null, imatriculation, type), PersonDataSource.clubPerson, PersonDataSource.guestPerson, null, null, null);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/csv/flights/data.csv")
        public void getActiveFlightsReturnsOnlyFlightsWithLandingTimeSetToNull(String landingTime, String takeoffTime, String imatriculation, String type) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime landing = null;
            LocalDateTime takeoff = null;

            if (!landingTime.equals("nill"))
                landing = LocalDateTime.parse(landingTime, formatter);
            if (!takeoffTime.equals("nill"))
                takeoff = LocalDateTime.parse(takeoffTime, formatter);
            if (imatriculation.equals("nill"))
                imatriculation = null;
            if (type.equals("nill"))
                type = null;

            LocalDateTime nowTime = LocalDateTime.parse("2020-12-09T12:30:00", formatter);

            List<FlightTo> expectedFlights = new ArrayList<>();
            List<Flight> flights = new ArrayList<>();

            Flight flight = generateFlight(1L, type, imatriculation, landing, takeoff);
            FlightTo flightTo = generateFlightTo(1L, type, imatriculation, landing, takeoff);

            if (landing == null) {
                expectedFlights.add(flightTo);
                flights.add(flight);
            }

            // mocks
            Mockito.doReturn(nowTime).when(clock).now();
            Mockito.doReturn(flights).when(flightRepository).findAllByLandingTimeNullOrderByTakeoffTimeAscIdAsc();

            assertEquals(testSubject.getFlightsInTheAir(), expectedFlights);
        }


        @ParameterizedTest
        @CsvFileSource(resources = "/csv/flights/data.csv")
        public void getActiveFlightsDoesNotReturnFlightsWithLandingTimeExceedingNow(String landingTime, String takeoffTime, String imatriculation, String type) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime landing = null;
            LocalDateTime takeoff = null;

            if (!landingTime.equals("nill"))
                landing = LocalDateTime.parse(landingTime, formatter);
            if (!takeoffTime.equals("nill"))
                takeoff = LocalDateTime.parse(takeoffTime, formatter);
            if (imatriculation.equals("nill"))
                imatriculation = null;
            if (type.equals("nill"))
                type = null;

            LocalDateTime nowTime = LocalDateTime.parse("2020-12-09T12:30:00", formatter);

            List<FlightTo> expectedFlights = new ArrayList<>();
            List<Flight> flights = new ArrayList<>();

            Flight flight = generateFlight(1L, type, imatriculation, landing, takeoff);
            FlightTo flightTo = generateFlightTo(1L, type, imatriculation, landing, takeoff);

            if (landing == null || landing.isAfter(nowTime)) {
                expectedFlights.add(flightTo);
                flights.add(flight);
            }

            // mocks
            Mockito.doReturn(nowTime).when(clock).now();
            Mockito.doReturn(flights).when(flightRepository).findAllByLandingTimeNullOrderByTakeoffTimeAscIdAsc();

            assertEquals(testSubject.getFlightsInTheAir(), expectedFlights);
        }

    }

    // EXPORT
    @Nested
    @DisplayName("Flights export")
    class FlightsExport {

        private Flight generateFlight(Long id, Flight.Type flightType, Task task, String type, String imatriculation) {
            return new Flight(id, flightType, task, LocalDateTime.of(2020, 12, 2, 12, 30, 30), LocalDateTime.of(2020, 12, 2, 16, 30, 30), new Airplane( null, imatriculation, type), PersonDataSource.clubPerson, PersonDataSource.guestPerson, null, null, null);
        }

        @Test
        public void getFlightsForReportReturnsAllCorrectTuplesOfFlights() {
            Flight firstGliderFlight = generateFlight(1L, Flight.Type.GLIDER, Task.of("A3"), "Zlín Z-42M", "OK-VGD32");
            Flight firstTowplaneFlight = generateFlight(2L, Flight.Type.TOWPLANE, Task.TOWPLANE_TASK, "ASW 15 B", "OK-V23424");

            Flight secondGliderFlight = generateFlight(3L, Flight.Type.GLIDER, Task.of("A1"), "L-13A Blaník", "OK-B123");
            Flight secondTowplaneFlight = generateFlight(4L, Flight.Type.TOWPLANE, Task.TOWPLANE_TASK, "ASW 15 B", "OK-C453");

            Flight thirdFlightWithoutGlider = generateFlight(5L, Flight.Type.TOWPLANE, Task.TOWPLANE_TASK, "ASW 15 B", "OK-AB567");

            firstTowplaneFlight.setGliderFlight(firstGliderFlight);
            secondTowplaneFlight.setGliderFlight(secondGliderFlight);

            List<Flight> flights = new ArrayList<>();
            flights.add(firstTowplaneFlight);
            flights.add(secondTowplaneFlight);
            flights.add(thirdFlightWithoutGlider);

            List<FlightTuppleTo> flightTuppleTos = new ArrayList<>();
            flightTuppleTos.add(FlightTuppleTo.builder().towplane(FlightTo.fromEntity(firstTowplaneFlight)).glider(FlightTo.fromEntity(firstGliderFlight)).build());
            flightTuppleTos.add(FlightTuppleTo.builder().towplane(FlightTo.fromEntity(secondTowplaneFlight)).glider(FlightTo.fromEntity(secondGliderFlight)).build());
            flightTuppleTos.add(FlightTuppleTo.builder().towplane(FlightTo.fromEntity(thirdFlightWithoutGlider)).build());

            Mockito.doReturn(flights).when(flightRepository).findAllByFlightTypeOrderByTakeoffTimeDescIdAsc(Mockito.eq(Flight.Type.TOWPLANE));


            List<FlightTuppleTo> flightTuppleTosActual = testSubject.getFlightsForReport();

            assertEquals(flightTuppleTos, flightTuppleTosActual);
        }

        @Test
        public void getFlightsForReportReturnsOnlyTowplanesIfGliderIsNotSpecified() {
            Flight firstTowplaneFlight = generateFlight(2L, Flight.Type.TOWPLANE, Task.TOWPLANE_TASK, "ASW 15 B", "OK-V23424");
            Flight secondTowplaneFlight = generateFlight(4L, Flight.Type.TOWPLANE, Task.TOWPLANE_TASK, "ASW 15 B", "OK-C453");
            Flight thirdFlightWithoutGlider = generateFlight(5L, Flight.Type.TOWPLANE, Task.TOWPLANE_TASK, "ASW 15 B", "OK-AB567");


            List<Flight> flights = new ArrayList<>();
            flights.add(firstTowplaneFlight);
            flights.add(secondTowplaneFlight);
            flights.add(thirdFlightWithoutGlider);

            List<FlightTuppleTo> flightTuppleTos = new ArrayList<>();
            flightTuppleTos.add(FlightTuppleTo.builder().towplane(FlightTo.fromEntity(firstTowplaneFlight)).build());
            flightTuppleTos.add(FlightTuppleTo.builder().towplane(FlightTo.fromEntity(secondTowplaneFlight)).build());
            flightTuppleTos.add(FlightTuppleTo.builder().towplane(FlightTo.fromEntity(thirdFlightWithoutGlider)).build());

            Mockito.doReturn(flights).when(flightRepository).findAllByFlightTypeOrderByTakeoffTimeDescIdAsc(Mockito.eq(Flight.Type.TOWPLANE));

            List<FlightTuppleTo> flightTuppleTosActual = testSubject.getFlightsForReport();

            assertEquals(flightTuppleTos, flightTuppleTosActual);
        }
    }
}