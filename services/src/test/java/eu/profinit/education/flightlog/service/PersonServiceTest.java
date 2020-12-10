package eu.profinit.education.flightlog.service;

import eu.profinit.education.flightlog.dao.ClubDatabaseDao;
import eu.profinit.education.flightlog.dao.User;
import eu.profinit.education.flightlog.domain.entities.Address;
import eu.profinit.education.flightlog.domain.entities.Person;
import eu.profinit.education.flightlog.domain.repositories.PersonRepository;
import eu.profinit.education.flightlog.exceptions.NotFoundException;
import eu.profinit.education.flightlog.service.datasource.PersonDataSource;
import eu.profinit.education.flightlog.to.AddressTo;
import eu.profinit.education.flightlog.to.PersonTo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Tag("fast")
@Tag("unit")
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private ClubDatabaseDao clubDatabaseDao;

    @InjectMocks
    private PersonServiceImpl testSubject;

    @BeforeEach
    public void setUp(){
        // init mocks
        MockitoAnnotations.initMocks(this);
        testSubject = new PersonServiceImpl(personRepository, clubDatabaseDao);
    }

    // getExistingOrCreatePerson

    @Nested
    @DisplayName("Return existing or create new person tests")
    class GetExistingOrCreatePerson {
        @ParameterizedTest
        @CsvSource({
            "41108,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,Hradec",
            "40008,jmenodlouhepadesatznakuaajmenodelsinezpadesatznak,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,Hradec",
            "99999,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznak,Zahradni,Hradec"
        })
        public void shouldCreateGuest(String postalCode, String name, String surname, String street, String city) {
            Person expectedPerson = new Person(Person.Type.GUEST, name, surname, new Address(street, city, postalCode, null));
            // prepare data
            PersonTo guestToCreate = PersonTo.builder()
                .firstName(name)
                .lastName(surname)
                .address(AddressTo.builder()
                    .street(street)
                    .city(city)
                    .postalCode(postalCode)
                    .build())
                .build();

            // mock behaviour
            when(personRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

            // call tested method
            Person person = testSubject.getExistingOrCreatePerson(guestToCreate);

            // verify results
            assertEquals(Person.Type.GUEST, person.getPersonType(), "Person type does not match");

            assertAll("Person",
                () -> assertEquals(expectedPerson.getFirstName(), person.getFirstName()),
                () -> assertEquals(expectedPerson.getLastName(), person.getLastName()),
                () -> assertEquals(expectedPerson.getFullName(), person.getFullName()),
                () -> assertEquals(expectedPerson.getAddress().getPostalCode(), person.getAddress().getPostalCode()),
                () -> assertEquals(expectedPerson.getAddress().getStreet(), person.getAddress().getStreet()),
                () -> assertEquals(expectedPerson.getAddress().getCountry(), person.getAddress().getCountry()),
                () -> assertEquals(expectedPerson.getAddress().getCity(), person.getAddress().getCity()));

        }

        @ParameterizedTest
        @CsvSource({
            "4, 41108,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,Hradec",
            "5, 40008,jmenodlouhepadesatznakuaajmenodelsinezpadesatznak,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,Hradec",
            "6, 99999,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznak,Zahradni,Hradec"
        })
        public void shouldCreateNewClubMember(Long id, String postalCode, String name, String surname, String street, String city) {
            // prepare data
            PersonTo newMember = PersonTo.builder()
                .memberId(id)
                .firstName(name)
                .lastName(surname)
                .address(AddressTo.builder()
                    .street(street)
                    .city(city)
                    .postalCode(postalCode)
                    .build())
                .build();

            User testUser = new User(id, name, surname, Arrays.asList("PILOT"));
            Person expectedPerson = new Person(Person.Type.CLUB_MEMBER, testUser.getFirstName(), testUser.getLastName(), new Address(street, city, postalCode, null));

            // mock behaviour
            when(personRepository.findByMemberId(id)).thenReturn(Optional.empty());
            when(clubDatabaseDao.getUsers()).thenReturn(Collections.singletonList(testUser));
            when(personRepository.save(any())).thenReturn(expectedPerson);

            // call tested method
            Person person = testSubject.getExistingOrCreatePerson(newMember);

            // verify results
            assertEquals(expectedPerson, person);
        }

        @ParameterizedTest
        @CsvSource(value = {
            "41108,jmenodelsinezpadesatznakuajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,Hradec",
            "41108,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,nazevulicekteryjedelsinezpadesatruznychznakunazevul,Hradec",
            "41108,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,ul,Hradec",
            "4110,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,Hradec",
            "411080,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,Hradec",
            "41S44,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,Hradec",
            "41108,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,a",
            "41108,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,nazevmestakteryjedelsinezpadesatruznychznakunazevul",
            "41108,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdelsinezpadesatznakuaprijmdelsinezpadesatznaku,Zahradni,Hradec",
        })
        public void shouldNotCreateGuestAndValidationExceptionShouldBeRaised(String postalCode, String name, String surname, String street, String city) {

            // prepare data
            PersonTo guestToCreate = PersonTo.builder()
                .firstName(name)
                .lastName(surname)
                .address(AddressTo.builder()
                    .street(street)
                    .city(city)
                    .postalCode(postalCode)
                    .build())
                .build();

            // mock behaviour
            when(personRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

            // call tested method
            assertThrows(Exception.class, () ->
                testSubject.getExistingOrCreatePerson(guestToCreate));
        }

        @ParameterizedTest
        @CsvSource(value = {
            "41108,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,nill,Hradec",
            "nill,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,Hradec",
            "41108,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,nill"
        })
        public void shouldNotCreatePersonWithNullFieldsInAddressAndNullPointerExceptionShouldBeRaised(String postalCode, String name, String surname, String street, String city) {
            if (postalCode.equals("nill"))
                postalCode = null;
            if (name.equals("nill"))
                name = null;
            if (surname.equals("nill"))
                surname = null;
            if (street.equals("nill"))
                street = null;
            if (city.equals("nill"))
                city = null;

            // call tested method
            String finalName = name;
            String finalSurname = surname;
            String finalStreet = street;
            String finalCity = city;
            String finalPostalCode = postalCode;

            PersonTo person = PersonTo.builder()
                .firstName(finalName)
                .lastName(finalSurname)
                .address(AddressTo.builder()
                    .street(finalStreet)
                    .city(finalCity)
                    .postalCode(finalPostalCode)
                    .build())
                .build();


            when(personRepository.findByMemberId(Mockito.any())).thenReturn(Optional.empty());
            when(personRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

            // call tested method
            assertThrows(NullPointerException.class, () -> testSubject.getExistingOrCreatePerson(person));
        }

        @ParameterizedTest
        @CsvSource({
            "4, 41108,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,Hradec",
            "5, 40008,jmenodlouhepadesatznakuaajmenodelsinezpadesatznak,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,Hradec",
            "6, 99999,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,prijmdlouhepadesatznakuaajmenodelsinezpadesatznak,Zahradni,Hradec"
        })
        public void shouldReturnExistingClubMember(Long id, String postalCode, String name, String surname, String street, String city) {
            // prepare data
            PersonTo existingMember = PersonTo.builder()
                .memberId(id)
                .firstName(name)
                .lastName(surname)
                .address(AddressTo.builder()
                    .street(street)
                    .city(city)
                    .postalCode(postalCode)
                    .build())
                .build();

            User testUser = new User(id, name, surname, Arrays.asList("PILOT"));
            Person clubMemberFromDd = Person.builder().personType(Person.Type.CLUB_MEMBER).memberId(id).build();

            when(personRepository.findByMemberId(id)).thenReturn(Optional.of(clubMemberFromDd));
            when(clubDatabaseDao.getUsers()).thenReturn(Arrays.asList(testUser));

            Person person = testSubject.getExistingOrCreatePerson(existingMember);

            assertSame(clubMemberFromDd, person, "Should return prepared instance");
        }


        @ParameterizedTest
        @CsvSource(value = {
            "41108,nill,prijmdlouhepadesatznakuaajmenodelsinezpadesatznaku,Zahradni,Hradec",
            "41108,jmenodlouhepadesatznakuaajmenodelsinezpadesatznaku,nill,Zahradni,Hradec"
        })
        public void shouldNotCreatePersonAndNullPointerExceptionShouldBeRaised(String postalCode, String name, String surname, String street, String city) {
            if (postalCode.equals("nill"))
                postalCode = null;
            if (name.equals("nill"))
                name = null;
            if (surname.equals("nill"))
                surname = null;
            if (street.equals("nill"))
                street = null;
            if (city.equals("nill"))
                city = null;

            // mock behaviour
            when(personRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

            // call tested method
            String finalName = name;
            String finalSurname = surname;
            String finalStreet = street;
            String finalCity = city;
            String finalPostalCode = postalCode;

            Exception exception = assertThrows(NullPointerException.class, () -> PersonTo.builder()
                .firstName(finalName)
                .lastName(finalSurname)
                .address(AddressTo.builder()
                    .street(finalStreet)
                    .city(finalCity)
                    .postalCode(finalPostalCode)
                    .build())
                .build());

            assertTrue(exception.getMessage().endsWith("is marked non-null but is null"));
        }
    }

    // getClubMembers

    @Nested
    @DisplayName("Get club members")
    class GetClubMembers {

        @Test
        public void getClubMembersReturnsCorrectMembersMarkedAsClubMembers() {
            List<User> members = new ArrayList<>();
            members.add(new User(1L, "Karel", "Novák", Collections.singletonList("Zkušební pilot")));
            members.add(new User(2L, "Jan", "Dvořák", Collections.singletonList("Letecký instruktor")));
            members.add(new User(3L, "Jan", "Pavel", Collections.singletonList("Správce pokladny")));

            List<PersonTo> expectedMembers = new ArrayList<>();
            expectedMembers.add(new PersonTo(1L, "Karel", "Novák", null));
            expectedMembers.add(new PersonTo(2L, "Jan", "Dvořák", null));
            expectedMembers.add(new PersonTo(3L, "Jan", "Pavel", null));

            Mockito.doReturn(members).when(clubDatabaseDao).getUsers();

            assertEquals(expectedMembers, testSubject.getClubMembers());
        }
    }

    // getClubMemberById

    @Nested
    @DisplayName("Get club member by id")
    class GetClubMemberById {

        @Test
        public void getClubMemberByIdReturnsCorrectMember() {
            List<User> members = new ArrayList<>();
            members.add(new User(1L, "Karel", "Novák", Collections.singletonList("Zkušební pilot")));
            members.add(new User(2L, "Jan", "Dvořák", Collections.singletonList("Letecký instruktor")));
            members.add(new User(3L, "Jan", "Pavel", Collections.singletonList("Správce pokladny")));

            Mockito.doReturn(members).when(clubDatabaseDao).getUsers();

            assertEquals(ReflectionTestUtils.invokeMethod(testSubject, "getClubMemberById", 2L), members.get(1));
        }

        @Test
        public void getClubMemberByIdThrowsNotFoundExceptionForNonExistingMember() {
            List<User> members = new ArrayList<>();
            members.add(new User(1L, "Karel", "Novák", Collections.singletonList("Zkušební pilot")));
            members.add(new User(2L, "Jan", "Dvořák", Collections.singletonList("Letecký instruktor")));
            members.add(new User(3L, "Jan", "Pavel", Collections.singletonList("Správce pokladny")));

            Mockito.doReturn(members).when(clubDatabaseDao).getUsers();

            assertThrows(NotFoundException.class, () -> ReflectionTestUtils.invokeMethod(testSubject, "getClubMemberById", 90L));
        }
    }

    // createGuestEntity

    @Nested
    @DisplayName("Create guest entity from PersonTO")
    class CreateGuestEntity {

        @Test
        public void createGuestEntityCreatesCorrectGuestEntityWithAdress() {
            Person guestPersonExcepted = PersonDataSource.guestPerson;
            guestPersonExcepted.setId(null);

            Person person = ReflectionTestUtils.invokeMethod(testSubject, "createGuestEntity", PersonDataSource.guestPersonTo);

            assertAll("Person fields should be equal.",
                () -> assertEquals(person.getFirstName(), guestPersonExcepted.getFirstName()),
                () -> assertEquals(person.getLastName(), guestPersonExcepted.getLastName()),
                () -> assertEquals(person.getPersonType(), guestPersonExcepted.getPersonType()),
                () -> assertEquals(person.getFullAddress(), guestPersonExcepted.getFullAddress())
            );
        }

    }

}