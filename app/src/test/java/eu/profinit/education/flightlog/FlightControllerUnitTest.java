package eu.profinit.education.flightlog;

import eu.profinit.education.flightlog.domain.entities.FlightId;
import eu.profinit.education.flightlog.exceptions.ExternalSystemException;
import eu.profinit.education.flightlog.exceptions.NotFoundException;
import eu.profinit.education.flightlog.exceptions.ValidationException;
import eu.profinit.education.flightlog.service.CsvExportService;
import eu.profinit.education.flightlog.service.FlightService;
import eu.profinit.education.flightlog.to.FileExportTo;
import eu.profinit.education.flightlog.to.FlightTakeoffTo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = IntegrationTestConfig.class)
@Transactional
@Slf4j
@Tag("slow")
@Tag("unit")
public class FlightControllerUnitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private FlightService flightService;

    @MockBean
    private CsvExportService csvExportService;

    // TAKEOFF
    @Nested
    @DisplayName("flight/takeoff endpoint")
    class TakeoffEndpoint {

        @Test
        public void takeoffEndpointReturnsOkForCorrectRequest() throws IOException, URISyntaxException {
            Mockito.doNothing().when(flightService).takeoff(any(FlightTakeoffTo.class));

            String inputJson = readFileToString("takeoffInput.json");
            HttpEntity<String> request = createRequestEntityWithHeaders(inputJson);

            ResponseEntity<Object> flightsResponse = restTemplate.exchange("/flight/takeoff", HttpMethod.POST, request, Object.class);

            Assertions.assertEquals(HttpStatus.CREATED, flightsResponse.getStatusCode());
        }

        @Test
        public void takeoffEndpointReturnsBadRequestWhenEntityIsNotFound() throws IOException, URISyntaxException {
            Mockito.doThrow(NotFoundException.class).when(flightService).takeoff(any(FlightTakeoffTo.class));

            String inputJson = readFileToString("takeoffInput.json");
            HttpEntity<String> request = createRequestEntityWithHeaders(inputJson);

            ResponseEntity<Object> flightsResponse = restTemplate.exchange("/flight/takeoff", HttpMethod.POST, request, Object.class);

            Assertions.assertEquals(HttpStatus.BAD_REQUEST, flightsResponse.getStatusCode());
        }

        @Test
        public void takeoffEndpointReturnsBadRequestWhenValidationFailsForEntity() throws IOException, URISyntaxException {
            Mockito.doThrow(ValidationException.class).when(flightService).takeoff(any(FlightTakeoffTo.class));

            String inputJson = readFileToString("takeoffInput.json");
            HttpEntity<String> request = createRequestEntityWithHeaders(inputJson);

            ResponseEntity<Object> flightsResponse = restTemplate.exchange("/flight/takeoff", HttpMethod.POST, request, Object.class);

            Assertions.assertEquals(HttpStatus.BAD_REQUEST, flightsResponse.getStatusCode());
        }

        @Test
        public void takeoffEndpointReturnsServiceUnavailableIfExternalDatabasesRefusesToReturnResults() throws IOException, URISyntaxException {
            Mockito.doThrow(ExternalSystemException.class).when(flightService).takeoff(any(FlightTakeoffTo.class));

            String inputJson = readFileToString("takeoffInput.json");
            HttpEntity<String> request = createRequestEntityWithHeaders(inputJson);

            ResponseEntity<Object> flightsResponse = restTemplate.exchange("/flight/takeoff", HttpMethod.POST, request, Object.class);

            Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE, flightsResponse.getStatusCode());
        }
    }

    // Landing
    @Nested
    @DisplayName("flight/land endpoint")
    class Landpoint {

        @Test
        public void landingEndpointReturnsOKForCorrectRequest() throws IOException, URISyntaxException {
            Mockito.doNothing().when(flightService).land(any(FlightId.class), any(LocalDateTime.class));

            String inputJson = readFileToString("landInput.json");
            HttpEntity<String> request = createRequestEntityWithHeaders(inputJson);

            ResponseEntity<Object> flightsResponse = restTemplate.exchange("/flight/land", HttpMethod.POST, request, Object.class);

            Assertions.assertEquals(HttpStatus.OK, flightsResponse.getStatusCode());
        }

        @Test
        public void landingEndpointReturnsBadRequestForIncorrectLandingInput() throws IOException, URISyntaxException {
            Mockito.doThrow(ValidationException.class).when(flightService).land(any(FlightId.class), any(LocalDateTime.class));

            String inputJson = readFileToString("landInput.json");
            HttpEntity<String> request = createRequestEntityWithHeaders(inputJson);

            ResponseEntity<Object> flightsResponse = restTemplate.exchange("/flight/land", HttpMethod.POST, request, Object.class);

            Assertions.assertEquals(HttpStatus.BAD_REQUEST, flightsResponse.getStatusCode());
        }
    }

    @Nested
    @DisplayName("flight/export endpoint")
    class Export {

        @Test
        public void flightsExportReturnsCorrectDataAndOkResponseWithCorrectEncoding() throws IOException, URISyntaxException {
            FileExportTo fileExportTo = new FileExportTo();
            fileExportTo.setContentType(MediaType.APPLICATION_JSON);
            fileExportTo.setFileName("export.csv");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

            csvPrinter.printRecord("{data:'test'}");
            csvPrinter.flush();
            byte[] data = byteArrayOutputStream.toByteArray();

            fileExportTo.setContent(data);

            Mockito.doReturn(fileExportTo).when(csvExportService).getAllFlightsAsCsv();

            ResponseEntity<byte[]> flightsResponse = restTemplate.exchange("/flight/export", HttpMethod.GET, null, byte[].class);

            Assertions.assertEquals(HttpStatus.OK, flightsResponse.getStatusCode());
            Assertions.assertEquals("["+fileExportTo.getContentType().getType() + "/" + fileExportTo.getContentType().getSubtype()+"]", flightsResponse.getHeaders().get("Content-Type").toString());
            Assertions.assertEquals("[attachment; filename="+fileExportTo.getFileName()+"]", flightsResponse.getHeaders().get("Content-Disposition").toString());
            Assertions.assertArrayEquals(data, flightsResponse.getBody());
        }
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
