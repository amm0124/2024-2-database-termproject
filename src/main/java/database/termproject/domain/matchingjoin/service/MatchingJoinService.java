package database.termproject.domain.matchingjoin.service;

import database.termproject.domain.matchingjoin.repository.MatchingJoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingJoinService {

    private final MatchingJoinRepository matchingJoinRepository;
}
