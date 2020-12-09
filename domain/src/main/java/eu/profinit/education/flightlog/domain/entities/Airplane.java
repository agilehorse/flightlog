package eu.profinit.education.flightlog.domain.entities;

import eu.profinit.education.flightlog.domain.JpaConstants;
import eu.profinit.education.flightlog.domain.codebooks.ClubAirplane;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import static lombok.AccessLevel.*;

@Embeddable
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor(access = PUBLIC)
public class Airplane {

    @OneToOne
    @JoinColumn(name = JpaConstants.Columns.REGISTERED_AIRPLANE_ID)
    private ClubAirplane clubAirplane;

    private String guestAirplaneImmatriculation;
    private String guestAirplaneType;

    public static Airplane clubAirplane(ClubAirplane clubAirplane) {
        return Airplane.builder().clubAirplane(clubAirplane).build();
    }

    public static Airplane guestAirplane(String immatriculation, String type) {
        return Airplane.builder().guestAirplaneImmatriculation(immatriculation).guestAirplaneType(type).build();
    }

    public String getSafeImmatriculation(){
        if (clubAirplane == null) {
            return guestAirplaneImmatriculation;
        } else {
           return clubAirplane.getImmatriculation();
        }
    }

    public String getSafeType(){
        if (clubAirplane != null) {
            return clubAirplane.getType() != null ? clubAirplane.getType().getType() : null;
        } else {
            return guestAirplaneType;
        }
    }
}
