package eu.profinit.education.flightlog.selenium.pageObjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.DefaultUrl;

@DefaultUrl("http://localhost:8081")
public class ApplicationHomePage extends PageObject {
    public static final Target ACTIVE_FLIGHTS = Target
        .the("Active flights")
        .locatedBy("/html/body/router-view/div/div/div/table/tbody");

    public static final Target ACTIVE_FLIGHT = Target
        .the("Remaining active flight")
        .locatedBy("/html/body/router-view/div/div/div/table/tbody/tr/td[6]/a[2]");

}

