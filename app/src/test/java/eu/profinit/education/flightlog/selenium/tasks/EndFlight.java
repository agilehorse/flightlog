package eu.profinit.education.flightlog.selenium.tasks;

import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.thucydides.core.annotations.Step;

import static eu.profinit.education.flightlog.selenium.pageObjects.ApplicationHomePage.ACTIVE_FLIGHT;

public class EndFlight implements Task {

    String flight;

    public EndFlight(String flight) {
        this.flight = flight;
    }

    public static EndFlight called(String flight) {
        return Instrumented.instanceOf(EndFlight.class).withProperties(flight);
    }

    @Step("{0} ends a flight called #thingToDo")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Click.on(ACTIVE_FLIGHT)
        );
    }

}
