package eu.profinit.education.flightlog.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.profinit.education.flightlog.to.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@Slf4j
@Tag("slow")
@Tag("integration")
public class FlightControllerTest {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    public void generateJson() throws JsonProcessingException {
        AirplaneTo towplane = AirplaneTo.builder().id(5L).immatriculation("OK125").type("Type engine").build();

        PersonTo pilotTow = PersonTo.builder()
            .memberId(123L)
            .firstName("Adalbert")
            .lastName("Kolínsk7")
            .address(AddressTo.builder()
                .street("Ulice 656")
                .city("Město")
                .postalCode("99909")
                .build())
            .build();

        AirplaneWithCrewTo towplaneWithCrew = AirplaneWithCrewTo.builder().airplane(towplane).note("Note towplane")
            .pilot(pilotTow).build();

        AirplaneTo glider = AirplaneTo.builder().id(6L).immatriculation("OKHDG").type("Type glider").build();

        PersonTo pilotGli = PersonTo.builder()
            .memberId(125L)
            .firstName("Eliška")
            .lastName("Kutnohorská")
            .address(AddressTo.builder()
                .street("Ulice 8798")
                .city("Jiné město")
                .postalCode("88889")
                .build())
            .build();

        AirplaneWithCrewTo gliderWithCrew = AirplaneWithCrewTo.builder().airplane(glider).note("Note glider")
            .pilot(pilotGli).build();

        FlightTakeoffTo start = FlightTakeoffTo.builder().task("Task A").towplane(towplaneWithCrew).glider(gliderWithCrew).takeoffTime(LocalDateTime.now()).build();

        jsonMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = jsonMapper.writeValueAsString(start);
        log.info("JSON: {}", json);

    }
}