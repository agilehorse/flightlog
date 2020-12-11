package eu.profinit.education.flightlog.selenium.tasks;

import eu.profinit.education.flightlog.selenium.pageObjects.ApplicationHomePage;
import eu.profinit.education.flightlog.selenium.pageObjects.ExportPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class StartWithExportPage implements Task {

    ExportPage exportPage;

    public static StartWithExportPage downloadExportPage() {
        return instrumented(StartWithExportPage.class);
    }

    @Override
    @Step("{0} starts with created flights and export page")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Open.browserOn().the(exportPage)
        );
    }

}
