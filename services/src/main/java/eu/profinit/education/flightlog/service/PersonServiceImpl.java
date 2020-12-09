package eu.profinit.education.flightlog.service;


import eu.profinit.education.flightlog.dao.ClubDatabaseDao;
import eu.profinit.education.flightlog.dao.User;
import eu.profinit.education.flightlog.domain.entities.Address;
import eu.profinit.education.flightlog.domain.entities.Person;
import eu.profinit.education.flightlog.domain.repositories.PersonRepository;
import eu.profinit.education.flightlog.exceptions.NotFoundException;
import eu.profinit.education.flightlog.exceptions.ValidationException;
import eu.profinit.education.flightlog.to.PersonTo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final ClubDatabaseDao clubDatabaseDao;

    @Override
    public List<PersonTo> getClubMembers() {
        return clubDatabaseDao.getUsers().stream().map(this::convert2To)
            .collect(Collectors.toList());
    }

    private User getClubMemberById(long memberId) {
        return clubDatabaseDao.getUsers().stream()
            .filter(user -> user.getMemberId() == memberId)
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Club member with ID {} does not exist.", memberId));
    }

    @Override
    public Person getExistingOrCreatePerson(PersonTo personTo) {
        if (personTo == null) {
            return null;
        }

        // check constraints
        personValidate(personTo);

        if (personTo.getMemberId() != null) {
            User userFromBackend = getClubMemberById(personTo.getMemberId());
            Optional<Person> maybeClubMember = personRepository.findByMemberId(userFromBackend.getMemberId());
            if (maybeClubMember.isPresent()) {
                return maybeClubMember.get();
            } else {
                Person clubMember = new Person(Person.Type.CLUB_MEMBER, personTo.getMemberId(), userFromBackend.getFirstName(), userFromBackend.getLastName());
                return personRepository.save(clubMember);
            }
        } else {
            Person guest = createGuestEntity(personTo);
            return personRepository.save(guest);
        }
    }

    private void personValidate(PersonTo personTo) {
        // validations
        if (personTo.getFirstName().length() > 50) {
            throw new ValidationException("First name must be maximum of 50 characters long.");
        }

        if (personTo.getLastName().length() > 50) {
            throw new ValidationException("Last name must be maximum of 50 characters long.");
        }

        if (personTo.getAddress() != null) {
            if (personTo.getAddress().getStreet().length() <= 3) {
                throw new ValidationException("Street must be longer than 3 characters.");
            }

            if (personTo.getAddress().getStreet().length() > 50) {
                throw new ValidationException("Street must be maximum of 50 characters long.");
            }

            if (personTo.getAddress().getCity().length() <= 1) {
                throw new ValidationException("City must be at least 2 characters long.");
            }

            if (personTo.getAddress().getCity().length() > 50) {
                throw new ValidationException("City must be maximum of 50 characters long.");
            }

            if (personTo.getAddress().getPostalCode().length() != 5) {
                throw new ValidationException("Postal code must be exactly 5 characters long.");
            }

            try {
                Integer.parseInt(personTo.getAddress().getPostalCode());
            } catch (NumberFormatException e) {
                throw new ValidationException("Postal code must only consist of numbers", e);
            }
        }
    }

    private Person createGuestEntity(PersonTo personTo) {
        Address address = null;
        if (personTo.getAddress() != null) {
            address = new Address(personTo.getAddress().getStreet(), personTo.getAddress().getCity(), personTo.getAddress().getPostalCode(), personTo.getAddress().getCountry());
        }
        return new Person(Person.Type.GUEST, personTo.getFirstName(), personTo.getLastName(), address);
    }

    private PersonTo convert2To(User user) {
        return new PersonTo(user.getMemberId(), user.getFirstName(), user.getLastName(), null);
    }

}
