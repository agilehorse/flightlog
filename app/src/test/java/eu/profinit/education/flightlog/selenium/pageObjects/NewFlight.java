package eu.profinit.education.flightlog.selenium.pageObjects;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.DefaultUrl;

@DefaultUrl("http://localhost:8081/#/newFlight")
public class NewFlight extends PageObject {

    public static final Target TOWPLANE_TAKEOFF_TIME = Target
        .the("Towplane takeoff time")
        .locatedBy("/html/body/router-view/div/form/div[1]/div[1]/input");

    public static final Target TOWPLANE_TASK = Target
        .the("Towplane task")
        .locatedBy("/html/body/router-view/div/form/div[1]/div[2]/input");

    public static final Target TOWPLANE_CLUB_AIRPLANE_SELECT = Target
        .the("Towplane club airplane select")
        .locatedBy("/html/body/router-view/div/form/compose[1]/compose/div[2]/div/select");

    public static final Target TOWPLANE_CLUB_PILOT_SELECT = Target
        .the("Towplane club pilot select")
        .locatedBy("/html/body/router-view/div/form/compose[1]/div[2]/compose/div[3]/div/select");

    public static final Target FLIGHT_START = Target
        .the("Start a flight")
        .locatedBy("/html/body/router-view/div/form/div[2]/div/button");

    public static final Target ACTIVE_FLIGHTS_TAB = Target
        .the("Active flights tab")
        .locatedBy("/html/body/div/div/ul/li[1]/a");
}
