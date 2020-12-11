package eu.profinit.education.flightlog.selenium;

import eu.profinit.education.flightlog.IntegrationTestConfig;
import eu.profinit.education.flightlog.selenium.driver.DriverFactory;
import eu.profinit.education.flightlog.selenium.tasks.EndFlight;
import eu.profinit.education.flightlog.selenium.tasks.StartWith;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.junit.*;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static eu.profinit.education.flightlog.selenium.questions.ActiveFlightsQuestion.activeFlights;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(SerenityRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = IntegrationTestConfig.class)
public class UserCanLandFlightTest {

    @Rule
    public SpringIntegrationMethodRule springIntegrationMethodRule = new SpringIntegrationMethodRule();

    private WebDriver driver;
    private Actor james = Actor.named("Karel");

    @Before
    public void before() throws IOException {
        driver = new DriverFactory().getDriver();
        givenThat(james).can(BrowseTheWeb.with(driver));
    }

    @Test
    public void shouldBeAbleToEndActiveFlights() {
        givenThat(james).wasAbleTo(StartWith.emptyActiveFlights());
        when(james).attemptsTo(EndFlight.called("1"));
        WebDriverWait wait = new WebDriverWait(driver, 5);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("/html/body/router-view/div/div/div/table/tbody/tr/td[6]/a[2]")
        ));

        then(james).should(seeThat(activeFlights(), equalTo("")));
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
