package eu.profinit.education.flightlog.rest;

import eu.profinit.education.flightlog.service.AirplaneService;
import eu.profinit.education.flightlog.to.AirplaneTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AirplaneController {

    // 3.1: Vystavte REST endpoint vracející seznam klubových letadel
    // letadla získáte voláním AirplaneService
    // bude se volat metoda GET na /airplane
    // struktura odpovědi je dána objektem AirplaneTo

    private final AirplaneService airplaneService;

    @GetMapping("/airplane")
    public List<AirplaneTo> getClubAirplanes() {
        List<AirplaneTo> clubAirplanes = airplaneService.getClubAirplanes();
        log.debug("Airplanes:\n{}", clubAirplanes);
        return clubAirplanes;
    }
}
