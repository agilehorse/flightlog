package eu.profinit.education.flightlog.to;

import eu.profinit.education.flightlog.domain.entities.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonTo implements Serializable {

    private Long memberId;
    private String firstName;
    private String lastName;
    private AddressTo address;

    public PersonTo(String firstName, String lastName, AddressTo address) {
        this.memberId = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public PersonTo(Long memberId) {
        this.memberId = memberId;
    }

    public static PersonTo fromEntity(Person entity) {
        if (entity == null) {
            return null;
        }
        return new PersonTo(entity.getMemberId(), entity.getFirstName(), entity.getLastName(), AddressTo.fromEntity(entity.getAddress()));
    }
}
