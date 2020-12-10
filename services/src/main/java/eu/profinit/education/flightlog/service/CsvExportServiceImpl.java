package eu.profinit.education.flightlog.service;

import eu.profinit.education.flightlog.domain.entities.Airplane;
import eu.profinit.education.flightlog.domain.entities.Flight;
import eu.profinit.education.flightlog.domain.entities.Person;
import eu.profinit.education.flightlog.domain.repositories.FlightRepository;
import eu.profinit.education.flightlog.exceptions.FlightLogException;
import eu.profinit.education.flightlog.to.FileExportTo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CsvExportServiceImpl implements CsvExportService {

    private final FlightRepository flightRepository;

    private final String fileName;
    private static final String DATE_PATTERN = "dd.MM.yyyy HH:mm:ss";
    private final String[] HEADERS = new String[]{"Datum", "Čas vzletu", "Typ letadla", "Imatrikulace", "Úloha letadla", "Datum a čas přistání", "Jméno a přijmení a adresa pilota", "Jméno a přijmení a adresa copilota"};

    public CsvExportServiceImpl(FlightRepository flightRepository, @Value("${csv.export.flight.fileName}") String fileName) {
        this.flightRepository = flightRepository;
        this.fileName = fileName;
    }

    @Override
    public FileExportTo getAllFlightsAsCsv() {
        List<Flight> all = flightRepository.findAllByFlightTypeOrderByTakeoffTimeDescIdAsc(Flight.Type.TOWPLANE);
        try (
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(HEADERS));
        ) {
            for (Flight towPlaneFlight : all) {
                Person towPlanePilot = towPlaneFlight.getPilot();
                Person towPlaneCopilot = towPlaneFlight.getCopilot();
                Airplane towPlane = towPlaneFlight.getAirplane();
                String copilotData = towPlaneCopilot == null ? "" : towPlaneCopilot.getFullName() + " " + towPlaneCopilot.getFullAddress();
                csvPrinter.printRecord(
                    formatDateTime(towPlaneFlight.getTakeoffTime()),
                    towPlane.getSafeType(),
                    towPlane.getSafeImmatriculation(),
                    towPlaneFlight.getTask().getValue(),
                    formatDateTime(towPlaneFlight.getLandingTime()),
                    (towPlanePilot.getFullName() + " " + towPlanePilot.getFullAddress()).trim(),
                    copilotData.trim());

                Flight gliderFlight = towPlaneFlight.getGliderFlight();
                if (gliderFlight != null) {
                    Airplane glider = towPlaneFlight.getAirplane();

                    Person gliderPilot = towPlaneFlight.getPilot();
                    Person gliderCopilot = towPlaneFlight.getCopilot();
                    String gliderCopilotData = gliderCopilot == null ? "" : gliderCopilot.getFullName() + " " + gliderCopilot.getFullAddress();
                    csvPrinter.printRecord(
                        formatDateTime(gliderFlight.getTakeoffTime()),
                        glider.getSafeType(),
                        glider.getSafeImmatriculation(),
                        gliderFlight.getTask().getValue(),
                        formatDateTime(gliderFlight.getLandingTime()),
                        (gliderPilot.getFullName() + " " + gliderPilot.getFullAddress()).trim(),
                        gliderCopilotData.trim());
                }
            }
            csvPrinter.flush();
            return new FileExportTo(fileName, MediaType.TEXT_PLAIN, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new FlightLogException("Error during flights CSV export", e);
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return formatter.format(dateTime);
    }
}
