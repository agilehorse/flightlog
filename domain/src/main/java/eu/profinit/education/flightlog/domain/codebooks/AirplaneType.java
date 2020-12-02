package eu.profinit.education.flightlog.domain.codebooks;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static eu.profinit.education.flightlog.domain.JpaConstants.Tables.AIRPLANE_TYPE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PUBLIC;

@Entity
@Getter
@Setter
@ToString
@Table(name = AIRPLANE_TYPE)
@NoArgsConstructor(access = PUBLIC)
@AllArgsConstructor(access = PUBLIC)
public class AirplaneType {

    @Id
    private Long id;
    @Column(unique = true)
    private String type;
    private int maxCapacity = 1;

}
