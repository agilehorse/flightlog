package eu.profinit.education.flightlog.dao;

import eu.profinit.education.flightlog.exceptions.ExternalSystemException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!stub")
public class ClubDatabaseDaoImpl implements ClubDatabaseDao {

    private final RestTemplate restTemplate;

    private final String clubDbBaseUrl;

    // 5.2: načtěte property integration.clubDb.baseUrl z application.properties (hint: CsvExportServiceImpl)
    public ClubDatabaseDaoImpl(@Value("${integration.clubDb.baseUrl}") String clubDbBaseUrl) {
        this.restTemplate = new RestTemplate();
        this.clubDbBaseUrl = clubDbBaseUrl;
    }


    @Override
    public List<User> getUsers() {
        User[] userList = {};
        try {
            //  5.3: implementujte tělo volání endpointu ClubDB pomocí REST template
            ResponseEntity<User[]> response = restTemplate.getForEntity(clubDbBaseUrl + "/club/user", User[].class);
            userList = response.getBody();
       } catch (RuntimeException e) {
            throw new ExternalSystemException("Cannot get users from Club database. URL: {}. Call resulted in exception.", e, clubDbBaseUrl);
        }
        return Arrays.asList(userList);
    }
}
