package database.termproject.domain.posting.controller;

import database.termproject.domain.posting.dto.request.MatchingTournamentPostingRequest;
import database.termproject.domain.posting.dto.request.PostingRequest;
import database.termproject.domain.posting.dto.response.PostingResponse;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.entity.PostingType;
import database.termproject.domain.posting.service.PostingServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/posting")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostingController {

    private final PostingServiceImpl postingService;

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER"})
    @PostMapping("/free")
    public ResponseEntity<?> createFreePosting(@RequestBody PostingRequest postingRequest) {
        Posting posting = postingService.createPosting(postingRequest, PostingType.FREE);
        return ResponseEntity.ok(PostingResponse.fromEntity(posting));
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER"})
    @PostMapping("/tip")
    public ResponseEntity<?> createTipPosting(@RequestBody PostingRequest postingRequest) {
        Posting posting = postingService.createPosting(postingRequest, PostingType.TIP);
        return ResponseEntity.ok(PostingResponse.fromEntity(posting));
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER"})
    @PostMapping("/matching")
    public ResponseEntity<?> createMatchingPosting(@RequestBody MatchingTournamentPostingRequest matchingTournamentPostingRequest) {
        postingService.createMatchingTournamentPosting(matchingTournamentPostingRequest, PostingType.MATCHING);
        return ResponseEntity.ok().build();
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @PostMapping("/tournament")
    public ResponseEntity<?> createTournamentPosting(@RequestBody MatchingTournamentPostingRequest matchingTournamentPostingRequest) {
        postingService.createMatchingTournamentPosting(matchingTournamentPostingRequest, PostingType.TOURNAMENT);
        return ResponseEntity.ok().build();
    }

    //TODO : promotion 내일 회의 해야 함
    

    //GET
    @GetMapping("/free")
    public ResponseEntity<?> getFreePosting() {
        return ResponseEntity.ok(
                postingService.getPosting(PostingType.FREE)
        );
    }

    @GetMapping("/tip")
    public ResponseEntity<?> getTipPosting() {
        return ResponseEntity.ok(
                postingService.getPosting(PostingType.TIP)
        );
    }



    //PUT



    //DELETE



}
