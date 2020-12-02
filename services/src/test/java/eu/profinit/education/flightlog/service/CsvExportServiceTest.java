package eu.profinit.education.flightlog.service;

import eu.profinit.education.flightlog.IntegrationTestConfig;
import eu.profinit.education.flightlog.to.FileExportTo;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegrationTestConfig.class)
@Transactional
@TestPropertySource(
    locations = "classpath:application-integrationtest.properties")
public class CsvExportServiceTest {

    @Autowired
    private CsvExportService testSubject;

    // 6.1: Odstrante anotaci @Ignore, aby se test vykonaval
    @Ignore("Tested method is not implemented yet")
    @Test
    public void testCSVExport() throws IOException, URISyntaxException {
        String fileName = "ExpectedExport.csv";
        String expected = readFileToString(fileName).replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));

        FileExportTo allFlightsAsCsv = testSubject.getAllFlightsAsCsv();
        String actual = new String(allFlightsAsCsv.getContent()).replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));

        //  6.2: zkontrolujte obsah CSV
        assertEquals(expected, actual);
    }

    private String readFileToString(String fileName) throws URISyntaxException, IOException {
        return Files.readString(Paths.get(getClass().getClassLoader().getResource(fileName).toURI()));
    }
}