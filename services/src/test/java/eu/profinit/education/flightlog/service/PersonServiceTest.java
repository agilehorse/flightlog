package eu.profinit.education.flightlog.service;

import eu.profinit.education.flightlog.IntegrationTestConfig;
import eu.profinit.education.flightlog.dao.ClubDatabaseDao;
import eu.profinit.education.flightlog.dao.User;
import eu.profinit.education.flightlog.domain.entities.Person;
import eu.profinit.education.flightlog.domain.repositories.PersonRepository;
import eu.profinit.education.flightlog.to.AddressTo;
import eu.profinit.education.flightlog.to.PersonTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = IntegrationTestConfig.class)
@ContextConfiguration(classes={IntegrationTestConfig.class})
@ActiveProfiles("integrationtest")
@Tag("fast")
@Tag("unit")
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private ClubDatabaseDao clubDatabaseDao;

    private PersonServiceImpl testSubject;

    @BeforeEach
    public void setUp(){
        testSubject = new PersonServiceImpl(personRepository, clubDatabaseDao);
    }

    @Test
    public void shouldCreateGuest() {
        // prepare data
        PersonTo guestToCreate = PersonTo.builder()
            .firstName("Jan")
            .lastName("Novák")
            .address(AddressTo.builder()
                .street("Tychonova 2")
                .city("Praha 6")
                .postalCode("16000")
                .build())
            .build();

        // mock behaviour
        when(personRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // call tested method
        Person person = testSubject.getExistingOrCreatePerson(guestToCreate);

        // verify results
        assertEquals(Person.Type.GUEST, person.getPersonType(), "Person type does not match");
        assertEquals(guestToCreate.getFirstName(), person.getFirstName(), "First name does not match");
        assertEquals(guestToCreate.getLastName(), person.getLastName(), "Last name does not match");

        assertEquals(guestToCreate.getAddress().getStreet(), person.getAddress().getStreet(), "Strear does not match");

    }

    @Test
    public void shouldReturnExistingClubMember() {
        // prepare data
        PersonTo existingClubMember = PersonTo.builder()
            .memberId(2L)
            .build();

        User testUser = new User(2L, "Kamila", "Spoustová", Arrays.asList("PILOT"));
        Person clubMemberFromDd = Person.builder().personType(Person.Type.CLUB_MEMBER).memberId(2L).build();

        // mock behaviour
        when(personRepository.findByMemberId(2L)).thenReturn(Optional.of(clubMemberFromDd));
        when(clubDatabaseDao.getUsers()).thenReturn(Arrays.asList(testUser));


        // call tested method
        Person person = testSubject.getExistingOrCreatePerson(existingClubMember);

        // verify results
        assertTrue(clubMemberFromDd == person, "Should return prepared instance");

    }

//    @Ignore("Test is not implemented")
    @Test
    public void shouldCreateNewClubMember() {
        // 7.1: Naimplementujte unit test s pouzitim mocku

        // prepare data
        PersonTo existingClubMember = PersonTo.builder().memberId(2L).build();
        User testUser = new User(2L, "Kamila", "Spoustová", Collections.singletonList("PILOT"));
        Person expectedPerson = new Person(Person.Type.CLUB_MEMBER, testUser.getFirstName(), testUser.getLastName(), null);

        // mock behaviour
        when(personRepository.findByMemberId(2L)).thenReturn(Optional.empty());
        when(clubDatabaseDao.getUsers()).thenReturn(Collections.singletonList(testUser));
        when(personRepository.save(any())).thenReturn(expectedPerson);

        // call tested method
        Person person = testSubject.getExistingOrCreatePerson(existingClubMember);

        // verify results
        assertEquals(expectedPerson, person);
    }
}