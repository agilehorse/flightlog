package eu.profinit.education.flightlog.service;

import eu.profinit.education.flightlog.domain.codebooks.AirplaneType;
import eu.profinit.education.flightlog.domain.codebooks.ClubAirplane;
import eu.profinit.education.flightlog.domain.entities.Airplane;
import eu.profinit.education.flightlog.domain.repositories.ClubAirplaneRepository;
import eu.profinit.education.flightlog.exceptions.ValidationException;
import eu.profinit.education.flightlog.to.AirplaneTo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirplaneServiceImpl implements AirplaneService {

    private final ClubAirplaneRepository clubAirplaneRepository;

    @Override
    public List<AirplaneTo> getClubAirplanes() {
        return clubAirplaneRepository.findAll(Sort.by("immatriculation")).stream()
            .map(this::validateInput).map(AirplaneTo::fromEntity).collect(Collectors.toList());
    }

    private ClubAirplane validateInput(ClubAirplane input) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ClubAirplane>> violations = validator.validate(input);
        Set<ConstraintViolation<AirplaneType>> typeViolations = validator.validate(input.getType());

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Validation exceptions raised when accessing airplanes from database. Please contact the administrator.", violations);
        }

        if (!typeViolations.isEmpty()) {
            throw new ConstraintViolationException("Validation exceptions raised when accessing airplanes from database. Please contact the administrator.", typeViolations);
        }
        return input;
    }
}
