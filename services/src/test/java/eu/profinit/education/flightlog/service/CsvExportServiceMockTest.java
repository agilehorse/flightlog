package eu.profinit.education.flightlog.service;

import eu.profinit.education.flightlog.domain.entities.Flight;
import eu.profinit.education.flightlog.domain.repositories.FlightRepository;
import eu.profinit.education.flightlog.to.FileExportTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Tag("fast")
@Tag("unit")
public class CsvExportServiceMockTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private CsvExportServiceImpl testSubject;

    @BeforeEach
    void setUp() {
        // init mocks
        MockitoAnnotations.initMocks(this);
    }

    @ParameterizedTest
    @MethodSource("eu.profinit.education.flightlog.service.datasource.FlightDataSource#validFlights")
    public void testCSVExport(Flight flight) throws IOException, URISyntaxException {
        // mock the flight repository
        Mockito.doReturn(Collections.singletonList(flight)).when(flightRepository).findAllByFlightTypeOrderByTakeoffTimeDescIdAsc(Flight.Type.TOWPLANE);

        String fileName = "csv/expectedExport" + flight.getId().getId() + ".csv";
        String expected = readFileToString(fileName)
                             .replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));

        FileExportTo allFlightsAsCsv = testSubject.getAllFlightsAsCsv();
        String actual = new String(allFlightsAsCsv.getContent())
                            .replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));

        assertEquals(expected, actual);
    }

    private String readFileToString(String fileName) throws URISyntaxException, IOException {
        return Files.readString(Paths.get(getClass().getClassLoader().getResource(fileName).toURI()));
    }
}