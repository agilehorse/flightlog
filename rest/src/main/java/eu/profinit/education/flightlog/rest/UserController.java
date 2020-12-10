package eu.profinit.education.flightlog.rest;

import eu.profinit.education.flightlog.exceptions.ExternalSystemException;
import eu.profinit.education.flightlog.exceptions.FlightLogException;
import eu.profinit.education.flightlog.service.PersonService;
import eu.profinit.education.flightlog.to.PersonTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final PersonService personService;

    @GetMapping("/user")
    public List<PersonTo> getClubMembers() {
        return personService.getClubMembers();
    }

    @ExceptionHandler({ ExternalSystemException.class})
    public ResponseEntity<String> handleExternalSystemException(FlightLogException e) {
        return status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
    }
}
