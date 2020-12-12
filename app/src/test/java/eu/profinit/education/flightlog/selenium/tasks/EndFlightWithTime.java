package eu.profinit.education.flightlog.selenium.tasks;

import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.Step;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static eu.profinit.education.flightlog.selenium.pageObjects.ApplicationHomePage.LANDING_TIME_INPUT;

public class EndFlightWithTime implements Task {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    String time = LocalDateTime.now().format(formatter); // defaults to the current time

    public EndFlightWithTime(String time) {
        this.time = time;
    }

    public static EndFlightWithTime called(String time) {
        return Instrumented.instanceOf(EndFlightWithTime.class).withProperties(time);
    }

    @Step("{0} ends a flight with manual time #time")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Click.on(Target
                .the("Enter manual end time button")
                .locatedBy("/html/body/router-view/div/div/div/table/tbody/tr/td[6]/a[1]")),
            Enter.theValue(time).into(LANDING_TIME_INPUT),
            Click.on(Target
                .the("Confirm button")
                .locatedBy("/html/body/router-view/div/div/div/div/div/div[2]/div[2]/input[1]"))
        );
    }

}
