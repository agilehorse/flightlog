package eu.profinit.education.flightlog.to;

import eu.profinit.education.flightlog.domain.entities.Address;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressTo {

    private String street;
    private String city;
    private String postalCode;
    private String country;

    public static AddressTo fromEntity(Address entity) {
        if (entity == null) {
            return null;
        }
        return new AddressTo(entity.getStreet(), entity.getCity(), entity.getPostalCode(), entity.getCountry());
    }
}
