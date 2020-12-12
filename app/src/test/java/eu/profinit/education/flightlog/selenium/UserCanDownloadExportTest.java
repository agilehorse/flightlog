package eu.profinit.education.flightlog.selenium;

import eu.profinit.education.flightlog.IntegrationTestConfig;
import eu.profinit.education.flightlog.selenium.driver.DriverFactory;
import eu.profinit.education.flightlog.selenium.tasks.DownloadExport;
import eu.profinit.education.flightlog.selenium.tasks.StartWithExportPage;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.when;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional()
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = IntegrationTestConfig.class)
public class UserCanDownloadExportTest {

    private final String PATH_DATA = System.getProperty("user.dir") + File.separator+ "src"+File.separator+"test"+File.separator+"resources"+File.separator+"data" + File.separator;

    @Autowired
    ServletContext context;

    @Rule
    public SpringIntegrationMethodRule springIntegrationMethodRule = new SpringIntegrationMethodRule();

    private Actor james = Actor.named("Kuba");
    private WebDriver driver;
    private String path;

    @Before
    public void before() throws IOException {
        path = System.getProperty("user.dir") + File.separator+ "src"+File.separator+"test"+File.separator+"resources"+File.separator+"download";
        driver = new DriverFactory().getDriver(path);
        givenThat(james).can(BrowseTheWeb.with(driver));
    }

    @Test
    public void shouldBeAbleToDownloadCorrectExportData() throws IOException, URISyntaxException {
        givenThat(james).wasAbleTo(StartWithExportPage.downloadExportPage());
        when(james).attemptsTo(DownloadExport.downloadExportPage());

        WebDriverWait wait = new WebDriverWait(driver, 3);
        try {
            wait.until(ExpectedConditions.urlContains("flight/export"));
        } catch (TimeoutException e) {} // do this little hack for download, because implicit wait does not work for some reason ¯\_(ツ)_/¯

        String inputJson = readFileToString(path + File.separator + "flights.csv");
        String expectedInput = readFileToString( PATH_DATA + "flights.csv");
        assertEquals(expectedInput, inputJson);
    }

    private String readFileToString(String fileName) throws IOException, URISyntaxException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    @After
    public void teardown() throws IOException {
        if (driver != null) {
            driver.quit();
        }
        Files.delete(Paths.get(path + File.separator + "flights.csv")); // clear downloaded files
    }
}
