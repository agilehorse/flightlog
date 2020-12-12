package eu.profinit.education.flightlog.to;

import eu.profinit.education.flightlog.domain.codebooks.ClubAirplane;
import eu.profinit.education.flightlog.domain.entities.Airplane;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirplaneTo implements Serializable {
    private Long id;
    private String immatriculation;
    private String type;


    public AirplaneTo(String immatriculation, String type) {
        this.immatriculation = immatriculation;
        this.type = type;
    }
    
    public static AirplaneTo fromEntity(Airplane entity){
        if(entity.getClubAirplane() != null) {
            // club airplane
            return new AirplaneTo(entity.getClubAirplane().getId(), entity.getClubAirplane().getImmatriculation(), entity.getClubAirplane().getType().getType());
        } else {
            return new AirplaneTo(entity.getGuestAirplaneImmatriculation(), entity.getGuestAirplaneType());
        }
    }

    public static AirplaneTo fromEntity(ClubAirplane entity){
        return new AirplaneTo(entity.getId(), entity.getImmatriculation(), entity.getType().getType());
    }
}
