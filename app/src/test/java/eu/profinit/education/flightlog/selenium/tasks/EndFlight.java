package eu.profinit.education.flightlog.selenium.tasks;

import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.Step;

public class EndFlight implements Task {

    String flight = "1"; // defaults to the first element

    public EndFlight(String flight) {
        this.flight = flight;
    }

    public static EndFlight called(String flight) {
        return Instrumented.instanceOf(EndFlight.class).withProperties(flight);
    }

    @Step("{0} ends a flight called #thingToDo")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Click.on(Target
                .the("Remaining active flight")
                .locatedBy("/html/body/router-view/div/div/div/table/tbody/tr["+flight+"]/td[6]/a[2]"))
        );
    }

}
