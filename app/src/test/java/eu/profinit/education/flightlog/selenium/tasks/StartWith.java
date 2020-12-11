package eu.profinit.education.flightlog.selenium.tasks;

import eu.profinit.education.flightlog.selenium.pageObjects.ApplicationHomePage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class StartWith implements Task {

    ApplicationHomePage applicationHomePage;

    public static StartWith activeFlights() {
        return instrumented(StartWith.class);
    }

    @Override
    @Step("{0} starts with no active flight")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Open.browserOn().the(applicationHomePage)
        );
    }
}

