package database.termproject.domain.posting.service;


import database.termproject.domain.posting.dto.request.MatchingEditRequest;
import database.termproject.domain.posting.dto.request.MatchingTournamentPostingRequest;
import database.termproject.domain.posting.entity.Matching;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.repository.MatchingRepository;
import database.termproject.global.error.ProjectError;
import database.termproject.global.error.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingRepository matchingRepository;

    @Transactional
    public Matching save(Posting posting, String when, String place, int limit) {

        Matching matching = Matching.builder()
                .posting(posting)
                .eventTime(when)
                .place(place) // 기본값 설정
                .capacity(limit)
                .build();

        matchingRepository.save(matching);
        return matching;
    }

    public Optional<Matching> findByPostingId(Long postingId){
        Optional<Matching> matching = matchingRepository.findByPostingId(postingId);
        return matching;
    }


    public Matching findByMatchingId(Long matchingId){
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new ProjectException(ProjectError.MATCHING_NOT_FOUND));
        return matching;
    }

    @Transactional
    public Matching update(MatchingEditRequest matchingEditRequest){
        Long matchingId = matchingEditRequest.matchingId();
        Matching matching = findByMatchingId(matchingId);
        matching.updateMatching(matchingEditRequest.eventTime(),
                matchingEditRequest.place(),
                matchingEditRequest.capacity());
        matchingRepository.save(matching);
        return matching;
    }

}
