package eu.profinit.education.flightlog.selenium.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DownloadExport implements Task {

    public DownloadExport() {
    }

    public static DownloadExport downloadExportPage() {
        return instrumented(DownloadExport.class);
    }

    @Override
    @Step("{0} clicks on download export button")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Click.on(Target
                .the("Export button")
                .locatedBy("/html/body/router-view/div/div/a"))
        );
    }

}
