package eu.profinit.education.flightlog.selenium.pageObjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.DefaultUrl;

@DefaultUrl("http://localhost:8081/#/report")
public class ExportPage extends PageObject {

    public static final Target EXPORT_CSV = Target
        .the("Export csv button")
        .locatedBy("/html/body/router-view/div/div/a");
}

