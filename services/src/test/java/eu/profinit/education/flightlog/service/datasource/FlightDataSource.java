package eu.profinit.education.flightlog.service.datasource;

import eu.profinit.education.flightlog.domain.entities.Address;
import eu.profinit.education.flightlog.domain.entities.Airplane;
import eu.profinit.education.flightlog.domain.entities.Flight;
import eu.profinit.education.flightlog.domain.entities.Person;
import eu.profinit.education.flightlog.domain.fields.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class FlightDataSource {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static Stream<Flight> validFlights() {
        return Stream.of(
            new Flight( 1L,
                    Flight.Type.TOWPLANE,
                    Task.TOWPLANE_TASK,
                    LocalDateTime.parse("2018-10-24 17:30", formatter),
                    LocalDateTime.parse("2018-10-24 19:30", formatter),
                    Airplane.guestAirplane("OK-V23424", "Zlín Z-42M"),
                    new Person(Person.Type.GUEST, "Řehoř", "Novák", new Address("", "", "", "")),
                    new Person(Person.Type.GUEST, "Kamila", "Spoustová", new Address("", "", "", "")),
                    "",
                null,
                null
            ),
            new Flight( 2L,
                Flight.Type.TOWPLANE,
                Task.TOWPLANE_TASK,
                LocalDateTime.parse("2018-10-24 17:30", formatter),
                null,
                Airplane.guestAirplane("OK-V23424", "Zlín Z-42M"),
                new Person(Person.Type.GUEST, "Řehoř", "Novák", new Address("", "", "", "")),
                new Person(Person.Type.GUEST, "Kamila", "Spoustová", new Address("", "", "", "")),
                "",
                null,
                null
            ),
            new Flight( 3L,
                Flight.Type.TOWPLANE,
                Task.TOWPLANE_TASK,
                LocalDateTime.parse("2018-10-24 17:30", formatter),
                null,
                Airplane.guestAirplane("OK-V23424", "Zlín Z-42M"),
                new Person(Person.Type.GUEST, "Řehoř", "Novák", new Address("", "", "", "")),
                new Person(Person.Type.GUEST, "Kamila", "Spoustová", new Address("", "", "", "")),
                "",
                null,
                new Flight( 4L,
                    Flight.Type.GLIDER,
                    Task.of(""),
                    LocalDateTime.parse("2018-10-24 17:30", formatter),
                    null,
                    Airplane.guestAirplane("OK-V23424", "Zlín Z-42M"),
                    new Person(Person.Type.GUEST, "Řehoř", "Novák", new Address("", "", "", "")),
                    new Person(Person.Type.GUEST, "Kamila", "Spoustová", new Address("", "", "", "")),
                    "",
                    null,
                    null
                )
            )
        );
    }

}
