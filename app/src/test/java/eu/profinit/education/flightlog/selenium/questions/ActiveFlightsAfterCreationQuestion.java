package eu.profinit.education.flightlog.selenium.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

import java.util.List;

import static eu.profinit.education.flightlog.selenium.pageObjects.ApplicationHomePage.ACTIVE_FLIGHTS_LIST;

public class ActiveFlightsAfterCreationQuestion  implements Question<List<String>> {

    public static Question<List<String>> theActiveFlights() {
        return new ActiveFlightsAfterCreationQuestion();
    }

    @Override
    public List<String> answeredBy(Actor actor) {
        return Text.of(ACTIVE_FLIGHTS_LIST).viewedBy(actor).asList();
    }
}

