package eu.profinit.education.flightlog.selenium;

import cz.cvut.fel.still.sqa.seleniumStarterPack.config.DriverFactory;
import eu.profinit.education.flightlog.selenium.tasks.EndFlight;
import eu.profinit.education.flightlog.selenium.tasks.StartWith;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static eu.profinit.education.flightlog.selenium.questions.ActiveFlightsQuestion.activeFlights;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(SerenityRunner.class)
public class SeleniumPoc {

    Actor james = Actor.named("James");

    private WebDriver driver;

    @Before
    public void before() throws IOException {
        driver = new DriverFactory().getDriver();

        givenThat(james).can(BrowseTheWeb.with(driver));
    }

    @Test
    public void shouldBeAbleToEndActiveFlights() {
        givenThat(james).wasAbleTo(StartWith.emptyActiveFlights());
        when(james).attemptsTo(EndFlight.called("1"));
        then(james).should(seeThat(activeFlights(), equalTo("")));
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
