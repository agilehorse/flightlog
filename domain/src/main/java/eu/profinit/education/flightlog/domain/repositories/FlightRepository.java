package eu.profinit.education.flightlog.domain.repositories;

import eu.profinit.education.flightlog.domain.entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findAllByFlightType(@NotNull Flight.Type flightType);

    List<Flight> findAllByLandingTimeNullOrderByTakeoffTimeAscIdAsc();

    List<Flight> findAllByFlightTypeOrderByTakeoffTimeDescIdAsc(@NotNull Flight.Type flightType);
}

