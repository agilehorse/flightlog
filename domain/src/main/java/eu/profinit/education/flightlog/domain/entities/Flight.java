package eu.profinit.education.flightlog.domain.entities;

import eu.profinit.education.flightlog.domain.JpaConstants;
import eu.profinit.education.flightlog.domain.fields.Task;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static eu.profinit.education.flightlog.domain.JpaConstants.Sequences;
import static eu.profinit.education.flightlog.domain.JpaConstants.Tables.FLIGHT;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PUBLIC;

@Entity
@Table(name = FLIGHT)
@SequenceGenerator(name = Sequences.FLIGHT, sequenceName = Sequences.FLIGHT, initialValue = JpaConstants.Sequences.INITIAL_VALUE)
@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor(access = PUBLIC)
@Getter
@Setter
@ToString
public class Flight {

    @Id
    @GeneratedValue(generator = JpaConstants.Sequences.FLIGHT)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Type flightType;

    private Task task;
    private LocalDateTime takeoffTime;
    private LocalDateTime landingTime;

    private Airplane airplane;

    @ManyToOne
    @JoinColumn(name = JpaConstants.Columns.PILOT_PERSON_ID)
    private Person pilot;

    @ManyToOne
    @JoinColumn(name = JpaConstants.Columns.COPILOT_PERSON_ID)
    private Person copilot;

    private String note;

    @OneToOne
    @JoinColumn(name = JpaConstants.Columns.TOWPLANE_FLIGHT_ID)
    private Flight towplaneFlight;

    @OneToOne
    @JoinColumn(name = JpaConstants.Columns.GLIDER_FLIGHT_ID)
    private Flight gliderFlight;

    public Flight(Type flightType, Task task, LocalDateTime takeoffTime, Airplane airplane, Person pilot, Person copilot, String note) {
        this.flightType = flightType;
        this.task = task;
        this.takeoffTime = takeoffTime;
        this.airplane = airplane;
        this.pilot = pilot;
        this.copilot = copilot;
        this.note = note;
    }

    public FlightId getId() {
        return new FlightId(id);
    }

    public enum Type {
        TOWPLANE,
        GLIDER
    }
}
