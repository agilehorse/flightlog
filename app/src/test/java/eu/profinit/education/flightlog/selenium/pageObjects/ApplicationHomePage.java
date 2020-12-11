package eu.profinit.education.flightlog.selenium.pageObjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.DefaultUrl;

@DefaultUrl("http://localhost:8081")
public class ApplicationHomePage extends PageObject {

    public static final Target ACTIVE_FLIGHTS = Target
        .the("Active flights")
        .locatedBy("/html/body/router-view/div/div/div/table/tbody");

    public static final Target LANDING_TIME_INPUT = Target
        .the("Manual landing time input")
        .locatedBy("/html/body/router-view/div/div/div/div/div/div[2]/div[1]/input");
}

