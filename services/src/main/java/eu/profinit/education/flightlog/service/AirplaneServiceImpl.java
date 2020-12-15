package eu.profinit.education.flightlog.service;

import eu.profinit.education.flightlog.domain.repositories.ClubAirplaneRepository;
import eu.profinit.education.flightlog.to.AirplaneTo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirplaneServiceImpl implements AirplaneService {

    private final ClubAirplaneRepository clubAirplaneRepository;

    @Override
    public List<AirplaneTo> getClubAirplanes() {
        return clubAirplaneRepository.findAll(Sort.by("immatriculation")).stream().map(AirplaneTo::fromEntity).collect(Collectors.toList());
    }
}
