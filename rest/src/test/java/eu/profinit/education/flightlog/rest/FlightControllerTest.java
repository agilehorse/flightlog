package eu.profinit.education.flightlog.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.profinit.education.flightlog.to.AirplaneTo;
import eu.profinit.education.flightlog.to.AirplaneWithCrewTo;
import eu.profinit.education.flightlog.to.FlightTakeoffTo;
import eu.profinit.education.flightlog.to.PersonTo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@Slf4j
public class FlightControllerTest {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    public void generateJson() throws JsonProcessingException {
        AirplaneTo towplane = AirplaneTo.builder().id(5L).immatriculation("OK125").type("Type engine").build();
        AirplaneWithCrewTo towplaneWithCrew = AirplaneWithCrewTo.builder().airplane(towplane).note("Note towplane").pilot(PersonTo.builder().firstName("Adalbert").lastName("Kolínsk7").memberId(123L).build()).build();
        AirplaneTo glider = AirplaneTo.builder().id(6L).immatriculation("OKHDG").type("Type glider").build();
        AirplaneWithCrewTo gliderWithCrew = AirplaneWithCrewTo.builder().airplane(glider).note("Note glider").pilot(PersonTo.builder().firstName("Eliška").lastName("Kutnohorská").memberId(125L).build()).build();

        FlightTakeoffTo start = FlightTakeoffTo.builder().task("Task A").towplane(towplaneWithCrew).glider(gliderWithCrew).takeoffTime(LocalDateTime.now()).build();

        jsonMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = jsonMapper.writeValueAsString(start);
        log.info("JSON: {}", json);

    }

}