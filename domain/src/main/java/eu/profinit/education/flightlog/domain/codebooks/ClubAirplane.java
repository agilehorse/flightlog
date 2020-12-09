package eu.profinit.education.flightlog.domain.codebooks;

import eu.profinit.education.flightlog.domain.JpaConstants;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static eu.profinit.education.flightlog.domain.JpaConstants.Tables.CLUB_AIRPLANE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PUBLIC;

@Entity
@Getter
@Setter
@ToString
@Table(name = CLUB_AIRPLANE)
@NoArgsConstructor(access = PUBLIC)
@AllArgsConstructor(access = PUBLIC)
public class ClubAirplane {

    @Id
    private Long id;

    @Column(unique = true)
    @Pattern(regexp = "(^OK-)\\w+")
    @Size(min = 5, max = 20)
    @NotNull
    private String immatriculation;

    @ManyToOne
    @JoinColumn(name = JpaConstants.Columns.TYPE_ID)
    private AirplaneType type;

    private boolean archived;


}
