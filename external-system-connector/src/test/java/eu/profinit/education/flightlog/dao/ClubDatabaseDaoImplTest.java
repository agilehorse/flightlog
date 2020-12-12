package eu.profinit.education.flightlog.dao;

import eu.profinit.education.flightlog.exceptions.ExternalSystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


@TestPropertySource(properties = {"integration.clubDb.baseUrl = http://vyuka.profinit.eu:8080"})
@ContextConfiguration
@Tag("slow")
@Tag("integration")
public class ClubDatabaseDaoImplTest {

    @Autowired
    private ClubDatabaseDao testSubject;

    @Test
    public void getUsers(){
        assumeTrue(testSubject != null, "Test is ignored because it requires clubDB server to run.");

        List<User> users = testSubject.getUsers();
        assertTrue(users.size() > 5, "Should contains at least 5 items.");
        assertNotNull(users.get(0).getFirstName());
        assertNotNull(users.get(0).getLastName());
        assertNotNull(users.get(0).getMemberId());
        assertNotNull(users.get(0).getRoles());
    }

    @Configuration
    @ComponentScan
    public static class IntegrationTestConfig {

    }
}