package database.termproject.domain.matchingjoin.service;

import database.termproject.domain.matchingjoin.dto.request.EditMatchingJoinRequest;
import database.termproject.domain.matchingjoin.dto.request.MatchingJoinRequest;
import database.termproject.domain.matchingjoin.dto.response.MatchingJoinResponse;
import database.termproject.domain.matchingjoin.entity.MatchingJoin;
import database.termproject.domain.matchingjoin.repository.MatchingJoinRepository;
import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting.entity.Matching;
import database.termproject.domain.posting.service.MatchingService;
import database.termproject.global.error.ProjectError;
import database.termproject.global.error.ProjectException;
import database.termproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static database.termproject.domain.posting.entity.PostingType.MATCHING;
import static database.termproject.global.error.ProjectError.*;

@Service
@RequiredArgsConstructor
public class MatchingJoinService {

    private final MatchingJoinRepository matchingJoinRepository;
    private final MatchingService matchingService;

    @Transactional
    public List<MatchingJoinResponse> createMatchingJoin(MatchingJoinRequest matchingJoinRequest) {
        Long matchingId = matchingJoinRequest.matchingId();
        Member member = getMember();

        Matching matching = matchingService.findByMatchingId(matchingId);

        if(matching.getPosting().getPostingType()==MATCHING && matchingJoinRequest.count() != null){
            throw new ProjectException(MATCHING_SINGLE_PLAYER_ONLY);
        }

        Integer count = matchingJoinRequest.count() != null ? matchingJoinRequest.count() : 1;

        if(matchingJoinRepository.existsByMatching_IdAndMember_Id(matchingId, member.getId())) {
            throw new ProjectException(ProjectError.MATCHING_ALREADY_EXISTS);
        }

        MatchingJoin matchingJoin = MatchingJoin.builder()
                .matching(matching)
                .member(member)
                .count(count)
                .build();

        matching.addNow(count);
        matchingJoinRepository.save(matchingJoin);

        return getMatchingJoins(matchingId);
    }

    @Transactional
    public void cancel(EditMatchingJoinRequest editMatchingJoinRequest){
        Long matchingJoinId = editMatchingJoinRequest.matchingJoinId();
        if(editMatchingJoinRequest.count() != null){
            throw new ProjectException(MATCHING_BAD_REQUEST);
        }

        MatchingJoin matchingJoin = matchingJoinRepository.findById(matchingJoinId)
                .orElseThrow(() -> new ProjectException(MATCHING_JOIN_NOT_FOUND));

        Matching matching = matchingJoin.getMatching();

        Long loginMemberId = getMember().getId();

        matchingJoin.validateMyRequest(loginMemberId);
        matching.validate(loginMemberId);
        matchingJoin.calculate(0);

        matchingJoinRepository.save(matchingJoin);
    }


    public List<MatchingJoinResponse> getMatchingJoins(Long matchingId) {
        List<MatchingJoin> matchingJoinList = matchingJoinRepository.findByMatchingId(matchingId);
        return matchingJoinList.stream()
                .map(MatchingJoinResponse::from)
                .collect(Collectors.toList());
    }


    private Member getMember(){
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        Object impl = authentication.getPrincipal();
        Member member = ((UserDetailsImpl) impl).getMember();
        return member;
    }






}
