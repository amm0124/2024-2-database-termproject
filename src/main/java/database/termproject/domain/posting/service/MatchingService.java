package database.termproject.domain.posting.service;


import database.termproject.domain.posting.dto.request.MatchingTournamentPostingRequest;
import database.termproject.domain.posting.entity.Matching;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.repository.MatchingRepository;
import database.termproject.domain.posting.repository.PostingRepository;
import database.termproject.global.error.ProjectError;
import database.termproject.global.error.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingRepository matchingRepository;

    public Matching save(Posting posting, MatchingTournamentPostingRequest matchingTournamentPostingRequest) {

        Matching matching = Matching.builder()
                .posting(posting)
                .when(matchingTournamentPostingRequest.when())
                .place(matchingTournamentPostingRequest.place())
                .limit(matchingTournamentPostingRequest.limit())
                .build();

        matchingRepository.save(matching);
        return matching;
    }

    public Optional<Matching> findByPostingId(Long postingId){
        Optional<Matching> matching = matchingRepository.findById(postingId);
        return matching;
    }


}
