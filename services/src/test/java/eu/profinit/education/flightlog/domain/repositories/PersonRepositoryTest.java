package eu.profinit.education.flightlog.domain.repositories;


import eu.profinit.education.flightlog.IntegrationTestConfig;
import eu.profinit.education.flightlog.domain.entities.Person;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = IntegrationTestConfig.class)
@Transactional
@Tag("slow")
@Tag("integration")
@TestPropertySource(
    locations = "classpath:application-integrationtest.properties")
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository testSubject;

    private Long testClubMemberId = 1L;

    @Test
    public void shouldFindClubMemberByMemberId() {
        Optional<Person> maybeClubMember = testSubject.findByMemberId(testClubMemberId);

        assertTrue(maybeClubMember.isPresent(), "Club member should be found");
        assertEquals(testClubMemberId, maybeClubMember.get().getMemberId(), "Member ID should be 1");

    }
}