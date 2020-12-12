package eu.profinit.education.flightlog.selenium;

import eu.profinit.education.flightlog.IntegrationTestConfig;
import eu.profinit.education.flightlog.selenium.driver.DriverFactory;
import eu.profinit.education.flightlog.selenium.tasks.EndFlightWithTime;
import eu.profinit.education.flightlog.selenium.tasks.StartWith;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static eu.profinit.education.flightlog.selenium.questions.ActiveFlightsQuestion.activeFlights;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(SerenityRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional()
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = IntegrationTestConfig.class)
public class UserCanLandFlightWithManualTimeTest {

    @Rule
    public SpringIntegrationMethodRule springIntegrationMethodRule = new SpringIntegrationMethodRule();

    private WebDriver driver;
    private Actor james = Actor.named("Kuba");

    @Before
    public void before() throws IOException {
        driver = new DriverFactory().getDriver();
        givenThat(james).can(BrowseTheWeb.with(driver));
    }

    @Test
    public void shouldBeAbleToEndActiveFlightWithManualTime() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String time = LocalDateTime.now().format(formatter);

        givenThat(james).wasAbleTo(StartWith.activeFlights());
        when(james).attemptsTo(EndFlightWithTime.called(time));
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
