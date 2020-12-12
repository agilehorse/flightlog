package eu.profinit.education.flightlog.dao;

import eu.profinit.education.flightlog.exceptions.ExternalSystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@Tag("fast")
@Tag("unit")
class ClubDatabaseExternalTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ClubDatabaseDaoImpl testSubject;

    private final String url = "http://test";

    @BeforeEach
    public void setUp(){
        testSubject = new ClubDatabaseDaoImpl(url);
        // init mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUsersThrowsExternalSystemExceptionIfExternalSystemDoesNotRespond() {
        Mockito.doThrow(RuntimeException.class).when(restTemplate).getForEntity(Mockito.any(), Mockito.any());

        Exception exception = assertThrows(ExternalSystemException.class, () -> testSubject.getUsers());
        assertEquals("Cannot get users from Club database. URL: "+url+". Call resulted in exception.", exception.getMessage());
    }
}