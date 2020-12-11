package eu.profinit.education.flightlog.selenium.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

import static eu.profinit.education.flightlog.selenium.pageObjects.ApplicationHomePage.ACTIVE_FLIGHTS;

public class ActiveFlightsQuestion implements Question<String> {

    public static Question<String> activeFlights() {
        return new ActiveFlightsQuestion();
    }

    @Override
    public String answeredBy(Actor actor) {
        return Text.of(ACTIVE_FLIGHTS).viewedBy(actor).asString();
    }
}
