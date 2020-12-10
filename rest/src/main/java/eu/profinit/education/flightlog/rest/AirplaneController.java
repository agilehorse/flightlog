package eu.profinit.education.flightlog.rest;

import eu.profinit.education.flightlog.exceptions.ExternalSystemException;
import eu.profinit.education.flightlog.exceptions.FlightLogException;
import eu.profinit.education.flightlog.exceptions.NotFoundException;
import eu.profinit.education.flightlog.exceptions.ValidationException;
import eu.profinit.education.flightlog.service.AirplaneService;
import eu.profinit.education.flightlog.to.AirplaneTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AirplaneController {

    private final AirplaneService airplaneService;

    @GetMapping("/airplane")
    public List<AirplaneTo> getClubAirplanes() {
        List<AirplaneTo> clubAirplanes = airplaneService.getClubAirplanes();
        log.debug("Airplanes:\n{}", clubAirplanes);
        return clubAirplanes;
    }

    @ExceptionHandler({ ConstraintViolationException.class})
    public ResponseEntity<String> handleInternalException(FlightLogException e) {
        return status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
