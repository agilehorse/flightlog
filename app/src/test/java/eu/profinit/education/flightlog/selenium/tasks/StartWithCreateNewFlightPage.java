package eu.profinit.education.flightlog.selenium.tasks;

import eu.profinit.education.flightlog.selenium.pageObjects.NewFlight;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class StartWithCreateNewFlightPage implements Task {

    NewFlight newFlight;

    public static StartWithCreateNewFlightPage createNewFlightPage() {
        return instrumented(StartWithCreateNewFlightPage.class);
    }

    @Override
    @Step("{0} starts with created flights and export page")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Open.browserOn().the(newFlight)
        );
    }

}
