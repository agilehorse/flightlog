package eu.profinit.education.flightlog.selenium.tasks;

import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Hit;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.Step;
import org.openqa.selenium.Keys;


import static eu.profinit.education.flightlog.selenium.pageObjects.NewFlight.*;

public class FillOutFlightInfo implements Task {

    String task = "Tažné letadlo";

    public FillOutFlightInfo() {
    }

    public static FillOutFlightInfo withDefault() {
        return Instrumented.instanceOf(FillOutFlightInfo.class).withProperties();
    }

    @Step("{0} creates flight with takeoff at #time with task #task")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Enter.theValue("12122020").into(TOWPLANE_TAKEOFF_TIME), // using third-party dropdown forces us to enter the date value this way
            Hit.the(Keys.ARROW_RIGHT).keyIn(TOWPLANE_TAKEOFF_TIME),
            Enter.theValue("1230").into(TOWPLANE_TAKEOFF_TIME),
            Enter.theValue(task).into(TOWPLANE_TASK),
            Click.on(Target
                .the("Select box")
                .locatedBy(TOWPLANE_CLUB_AIRPLANE_SELECT.getCssOrXPathSelector())),
            Click.on(Target
                .the("Second option")
                .locatedBy("/html/body/router-view/div/form/compose[1]/compose/div[2]/div/select/option[2]")),
            Click.on(TOWPLANE_CLUB_PILOT_SELECT),
            Click.on(Target
                .the("Third option")
                .locatedBy("/html/body/router-view/div/form/compose[1]/div[2]/compose/div[3]/div/select/option[3]")),
            Click.on(FLIGHT_START)
        );
    }


}
