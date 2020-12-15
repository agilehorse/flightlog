package eu.profinit.education.flightlog.domain.codebooks;

import eu.profinit.education.flightlog.domain.JpaConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static eu.profinit.education.flightlog.domain.JpaConstants.Tables.CLUB_AIRPLANE;
import static lombok.AccessLevel.PACKAGE;

@Entity
@Getter
@Setter
@ToString
@Table(name = CLUB_AIRPLANE)
@NoArgsConstructor(access = PACKAGE)
public class ClubAirplane {

    @Id
    private Long id;

    @Column(unique = true)
    private String immatriculation;

    @ManyToOne
    @JoinColumn(name = JpaConstants.Columns.TYPE_ID)
    private AirplaneType type;

    private boolean archived;


}
